(defn proto-get [obj key] (if (contains? obj key) (obj key) (proto-get (obj :prototype) key)))
(defn proto-call [this key & args] (apply (proto-get this key) (cons this args)))
(defn field [key] (fn [this] (proto-get this key)))
(defn method [key] (fn [this & args] (apply proto-call this key args)))

(def toString (method :toString))
(def evaluate (method :evaluate))
(def diff (method :diff))

; making Constant object ;
(def ConstPrototype
  (let [number (field :value)]
    {:toString (fn [this] (let [value (number this)] (format "%.1f" value)))
     :evaluate (fn [this _] (number this))}))
(defn Constant
  [number]
  {:prototype ConstPrototype
   :value     number})
(def CONSTANTS {:ZERO (Constant 0) :ONE (Constant 1) :TWO (Constant 2)})
(def ConstPrototype (assoc ConstPrototype :diff (fn [_ _] (CONSTANTS :ZERO))))

; making Variable object ;
(def VarPrototype
  (let [name (field :name)]
    {:toString (fn [this] (name this))
     :evaluate (fn [this vars] (vars (name this)))
     :diff (fn [this diffVar] (if (= (name this) diffVar) (CONSTANTS :ONE) (CONSTANTS :ZERO)))}))
(defn Variable
  [name]
  {:prototype VarPrototype
   :name      name})
(def VARIABLES {'x (Variable "x"), 'y (Variable "y"), 'z (Variable "z")})

; making AbstractOperation ;
(def AbstractOperPrototype
  (let [ops (field :operands) print (field :print) f (field :function) diffAction (field :diffAction)]
    {:toString (fn [this] (str "(" (print this) " " (clojure.string/join " " (mapv toString (ops this))) ")"))
     :evaluate (fn [this vars] (apply (f this) (mapv (fn [op] (evaluate op vars)) (ops this))))
     :diff     (fn [this diffVar] (if (= (count (ops this)) 2)
                                    ((diffAction this) ((ops this) 0) ((ops this) 1) (diff ((ops this) 0) diffVar)
                                      (diff ((ops this) 1) diffVar))
                                    ((diffAction this) ((ops this) 0) (diff ((ops this) 0) diffVar))))}))
(defn AbstractOperation
  [symbol f diffAction]
  (fn [& ops]
  {:prototype AbstractOperPrototype
   :print symbol
   :function f
   :diffAction diffAction
   :operands (vec ops)}))

; making Operations ;
(def Add
  (AbstractOperation '+
                     +
                     (fn [_ _ da db] (Add da db))))
(def Subtract
  (AbstractOperation '-
                     -
                     (fn [_ _ da db] (Subtract da db))))
(def Negate
  (AbstractOperation 'negate
                     -
                     (fn [_ da] (Negate da))))
(def Multiply
  (AbstractOperation '*
                     *
                     (fn [a b da db] (Add (Multiply a db)(Multiply b da)))))
(def Divide
  (AbstractOperation '/
                     (fn [a b] (/ a (double b)))
                     (fn [a b da db] (Divide (Subtract (Multiply b da) (Multiply a db)) (Multiply b b)))))
(def Square
  (AbstractOperation 'square
                     (fn [a] (* a a))
                     (fn [a da] (Multiply (Multiply (CONSTANTS :TWO) a) da))))
(def Sqrt
  (AbstractOperation 'sqrt
                     (fn [a] (Math/sqrt (Math/abs (double a))))
                     (fn [a da] (Divide (Multiply da a) (Multiply (CONSTANTS :TWO) (Sqrt (Multiply (Square a) a)))))))

; making parser ;
(def currentObjFn {'+ Add, '- Subtract, 'negate Negate, '* Multiply, '/ Divide, 'square Square, 'sqrt Sqrt})

(defn parseSequence [expression] (cond
                                   (seq? expression) (apply (currentObjFn (first expression)) (map parseSequence (rest expression)))
                                   (symbol? expression) (VARIABLES expression)
                                   (number? expression) (Constant expression)))
(def parseObject (comp parseSequence read-string))