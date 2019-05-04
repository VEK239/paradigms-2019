(defn coordinateChangingFn [f] (partial mapv f))
(defn shapelessFn [f] (fn [& shapes] (if (number? (first shapes)) (apply f shapes)
                                                                 (apply (coordinateChangingFn (shapelessFn f)) shapes))))
(def v+ (coordinateChangingFn +))
(def v- (coordinateChangingFn -))
(def v* (coordinateChangingFn *))

(defn v*s [vector scalar] (mapv (partial * scalar) vector))
(defn scalar [& vs] (apply + (apply v* vs)))
(defn vect [& vs] (apply (fn [v1 v2] [(- (* (v1 1) (v2 2)) (* (v1 2) (v2 1))) (- (* (v1 2) (v2 0)) (* (v1 0) (v2 2)))
                                      (- (* (v1 0) (v2 1)) (* (v1 1) (v2 0)))]) vs))
(def m+ (coordinateChangingFn v+))
(def m- (coordinateChangingFn v-))
(def m* (coordinateChangingFn v*))

(defn m*s [matrix scalar] (mapv (partial (fn [scalar vector] (v*s vector scalar)) scalar) matrix))
(defn m*v [matrix vector] (mapv (partial scalar vector) matrix))

(defn transpose [matrix] (apply mapv vector matrix))

(defn m*m [matrix1 matrix2] (mapv (fn [row1] (mapv (partial scalar row1) (transpose matrix2))) matrix1))

(def s+ (shapelessFn +))
(def s- (shapelessFn -))
(def s* (shapelessFn *))