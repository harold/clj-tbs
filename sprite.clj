(ns sprite
  (:import [javax.imageio ImageIO]
           [java.io File]))

(def g-sprites (ref []))

(defn new [in-path]
  (let [the-sprite (ref {:image (ImageIO/read (File. in-path))
                         :x 0
                         :y 0})]
    (dosync (alter g-sprites conj the-sprite))
    the-sprite))

(defn set-x [in-sprite x] (dosync (alter in-sprite assoc :x x)))
(defn set-y [in-sprite y] (dosync (alter in-sprite assoc :y y)))

(defn render-all [in-graphics]
  (doseq [the-sprite @g-sprites]
    (let [the-image (:image @the-sprite)
          the-sprite-half-width  (/ (.getWidth  the-image) 2)
          the-sprite-half-height (/ (.getHeight the-image) 2)
          x (int (- (:x @the-sprite) the-sprite-half-width))
          y (int (- (:y @the-sprite) the-sprite-half-height))]
      (.drawImage in-graphics (:image @the-sprite) x y nil))))
