(ns clj-tbs
  (:import [javax.imageio ImageIO]
           [java.io File]
           [javax.swing JFrame JPanel TransferHandler]
           [java.awt Color Dimension]
           [java.awt.image BufferedImage]))

(load-file "map1.clj")

(defn render-board [in-graphics w h]
  (let [map (map1/get-map)]
    (doseq [x (range 4)
            y (range 6)]
      (let [image (ImageIO/read (File. (str "images/" (nth (nth map y) x))))]
        (.drawImage in-graphics image (* x 128) (* y 128) nil)))))

(defn render [g in-panel]
  (let [w (.getWidth in-panel)
        h (.getHeight in-panel)
        the-image (new BufferedImage w h BufferedImage/TYPE_INT_ARGB)
        the-graphics (.getGraphics the-image)]
    (doto the-graphics
      (.setColor (Color. 60 60 60))
      (.fillRect 0 0 w h))
    (render-board the-graphics w h)
    (.drawImage g the-image 0 0 nil)
    (.dispose the-graphics)))

(defn main-panel []
  (let [the-return (proxy [JPanel] [] (paint [g] (render g this)))]
    (doto the-return
      (.setPreferredSize (Dimension. (* 128 4) (* 128 6))))))

(defn go []
  (let [frame (JFrame. "clj-tbs")
        panel (main-panel)]
    (doto frame (.add panel) .pack .show)))

(go)