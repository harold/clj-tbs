(ns clj-tbs
  (:import [javax.imageio ImageIO]
           [java.io File]
           [javax.swing JFrame JPanel TransferHandler]
           [java.awt Color Dimension]
           [java.awt.image BufferedImage]))

(defn render-board [in-graphics w h]
  (let [images ["G000M800.BMP" "G000M801.BMP" "G000M802.BMP" "G000M803.BMP"]]
    (doseq [x (range (/ (+ w 128) 128))
            y (range (/ (+ h 128) 128))]
      (doto in-graphics
        (.drawImage (ImageIO/read (File. (nth images (rand-int 4)))) (* x 128) (* y 128) nil)))))

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