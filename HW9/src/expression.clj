(defn changingFn [f] (fn [& vs] (fn [args] (apply f (map (fn [x] (x args)) vs)))))

(defn reduceFn [f] (fn [& args] (reduce (fn [a b] (f a b)) args)))

(def add (changingFn +))
(def subtract (changingFn -))
(def multiply (changingFn *))
(def divide (changingFn (fn [a b] (/ a (double b)))))
(def negate subtract)
(def square (changingFn (fn [a] (* a a))))
(def sqrt (changingFn (fn [a] (Math/sqrt (Math/abs (double a))))))
(def min (changingFn (reduceFn (fn [a b] (min a b)))))
(def max (changingFn (reduceFn (fn [a b] (max a b)))))

(defn constant [value] (fn [args] value))
(defn variable [name] (fn [args] (args name)))

(def currentFn {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'sqrt sqrt, 'square square,
                'min min, 'max max})

(defn parseSequence [expression] (cond
                                   (seq? expression) (apply (currentFn (first expression)) (map parseSequence (rest expression)))
                                   (symbol? expression) (variable (str expression))
                                   (number? expression) (constant expression)
                                   ))

(defn parseFunction (comp parseSequence read-string))
