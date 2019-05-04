(defn proto-get [obj key] (cond
                            (contains? obj key) (obj key)
                            (contains? obj :prototype) (proto-get (obj :prototype) key)))

(defn proto-call [this key & args] (apply (proto-get this key) (cons this args)))

(defn field [key] (fn [this] (proto-get this key)))

(defn method [key] (fn [this & args] (apply proto-call this key args)))


(def toString (method :toString))
(def evaluate (method :evaluate))
(def diff (method :diff))
(def operands (field :operands))

; making Constant object ;
(def ConstPrototype
  (let [number (field :value)]
    {:toString (fn [this] (let [value (number this)] (if (integer? number) (str value) (format "%.1f" value))))
     :evaluate (fn [this _] (number this))}))
(defn Constant
  [number]
  {:prototype ConstPrototype
   :value number})
;(def CONSTANTS {:ZERO })
(let [zero (Constant 0)]
  (def ConstPrototype (assoc ConstPrototype :diff (fn [_ _] zero))))

; making Variable object ;
(def VarPrototype
  (let [name (field :name) zero (Constant 0) one (Constant 1)]
    {:toString (fn [this] (name this))
     :evaluate (fn [this vars] (vars (name this)))
     :diff (fn [this diffVar] (if (= (name this) diffVar) one zero))}))
(defn Variable
  [name]
  {:prototype VarPrototype
   :name name})

; making AbstractOperation ;
(def AbstractOperPrototype
  (let [ops (field :operands) print (field :print) f (field :function) diffAction (method :diffAction)]
    {:toString (fn [this] (str "(" (print this) " " (clojure.string/join " " (mapv toString (ops this))) ")"))
     :evaluate (fn [this vars] (apply (f this) (mapv (fn [op] (evaluate op vars)) (ops this))))
     :diff (fn [this diffVar] (diffAction this diffVar))}))

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
                     (fn [this var] (apply Add (map (fn [op] (diff op var)) (operands this))))))
(def Subtract
  (AbstractOperation '-
                     -
                     (fn [this var] (apply Subtract (map (fn [op] (diff op var)) (operands this))))))
(def Negate
  (AbstractOperation 'negate
                     -
                     (fn [this var] (apply Subtract (map (fn [op] (diff op var)) (operands this))))))
(def Multiply
  (AbstractOperation '*
                     *
                     (fn [this var] (Add (Multiply ((operands this) 0) (diff ((operands this) 1) var))
                                              (Multiply ((operands this) 1) (diff ((operands this) 0) var))))))
(def Divide
  (AbstractOperation '/
                     (fn [a b] (/ a (double b)))
                     (fn [this var] (Divide (Subtract (Multiply ((operands this) 1) (diff ((operands this) 0) var))
                                                      (Multiply ((operands this) 0) (diff ((operands this) 1) var)))
                                            (Multiply ((operands this) 1) ((operands this) 1))))))

(def Square
  (AbstractOperation 'square
                     (fn [a] (* a a))
                     (fn [this var] (Add (Multiply ((operands this) 0) (diff ((operands this) 0) var))
                                         (Multiply ((operands this) 0) (diff ((operands this) 0) var))))))

(def Sqrt
  (AbstractOperation 'sqrt
                     (fn [a] (Math/sqrt (Math/abs (double a))))
                     (fn [this var] (Divide (Multiply (diff ((operands this) 0) var) ((operands this) 0))
                                      (Multiply (Constant 2) (Sqrt (Multiply (Square ((operands this) 0)) ((operands this) 0))))))))
; making parser ;
(def currentObjFn {'+ Add, '- Subtract, 'negate Negate, '* Multiply, '/ Divide, 'square Square, 'sqrt Sqrt})
(def variables {'x (Variable "x"), 'y (Variable "y"), 'z (Variable "z")})

(defn parseSequence [expression] (cond
                                   (seq? expression) (apply (currentObjFn (first expression)) (map parseSequence (rest expression)))
                                   (symbol? expression) (variables expression)
                                   (number? expression) (Constant expression)))
(def parseObject (comp parseSequence read-string))