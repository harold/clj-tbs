(ns clj-tbs
  (:import [javax.imageio ImageIO]
           [java.io File]
           [javax.swing JFrame JPanel TransferHandler]
           [java.awt Color Dimension]
           [java.awt.image BufferedImage]))

(load-file "map1.clj")

(defn build-map [in-map]
  (doseq [x (range 4)
          y (range 6)]
    (let [the-sprite (new-sprite (str "images\\" (nth (nth in-map y) x)))]
      (dosync (alter the-sprite assoc :x (- (* x 128) 192)
                                      :y (- (* y 128) 320))))))

(def g-sprites (ref []))

(defn new-sprite [in-path]
  (let [the-sprite (ref {:image (ImageIO/read (File. in-path))
                         :x 0
                         :y 0})]
    (dosync (alter g-sprites conj the-sprite))
    the-sprite))

(defn render-sprites [in-graphics w h]
  (doseq [the-sprite @g-sprites]
    (let [the-image (:image @the-sprite)
          the-sprite-half-width (/ (.getWidth the-image) 2)
          the-sprite-half-height (/ (.getHeight the-image) 2)
          x (int (+ (- (:x @the-sprite) the-sprite-half-width) (/ w 2)))
          y (int (+ (- (:y @the-sprite) the-sprite-half-height) (/ h 2)))]
      (.drawImage in-graphics (:image @the-sprite) x y nil))))

(defn render [g in-panel]
  (let [w (.getWidth in-panel)
        h (.getHeight in-panel)]
    (doto g
      (.setColor (Color. 60 60 60))
      (.fillRect 0 0 w h))
    (render-sprites g w h)))

(defn main-panel []
  (let [the-return (proxy [JPanel] [] (paint [g] (render g this)))]
    (doto the-return
      (.setPreferredSize (Dimension. (* 128 4) (* 128 6))))))

(defn go []
  (let [frame (JFrame. "clj-tbs")
        panel (main-panel)]
    (build-map (map1/get-map))
    (doto frame (.add panel) .pack .show)))

(go)
