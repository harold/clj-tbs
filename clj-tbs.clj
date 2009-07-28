(ns clj-tbs
  (:import [javax.imageio ImageIO]
           [java.io File]
           [javax.swing JFrame JPanel TransferHandler]
           [java.awt Color Dimension]
           [java.awt.image BufferedImage]))

(load-file "map1.clj")
(load-file "sprite.clj")

(defn build-map [in-map]
  (doseq [x (range 4)
          y (range 6)]
    (let [the-sprite (sprite/new (str "images\\" (nth (nth in-map y) x)))]
      (doto the-sprite
        (sprite/set-x (- (* x 128) 192))
        (sprite/set-y (- (* y 128) 320))))))

(defn render [g in-panel]
  (let [w (.getWidth in-panel)
        h (.getHeight in-panel)]
    (doto g
      (.setColor (Color. 60 60 60))
      (.fillRect 0 0 w h))
      (.translate (/ w 2) (/ h 2))
    (sprite/render-all g)))

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
