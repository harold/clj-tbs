(ns clj-tbs
  (:import [javax.imageio ImageIO]
           [java.io File]
           [javax.swing JFrame JPanel TransferHandler]
           [java.awt Color Dimension]
           [java.awt.image BufferedImage]))

(defn get-time [_] (/ (System/nanoTime) 1000000.0))

(def last-time (ref (get-time 0)))
(def filtered-time (ref 16))

(def image (ImageIO/read (File. "images/G000M800.BMP")))
(def theta (ref 0))

(defn update-filtered-time [current]
  (let [elapsed (- (get-time 0) @last-time)]
    (dosync (alter last-time get-time))
    (- current (* (- current elapsed) 0.1))))

(defn render [g in-panel]
  (dosync (alter filtered-time update-filtered-time))
  (let [w (.getWidth in-panel)
        h (.getHeight in-panel)
        t @theta
        r 200
        p1 {:x (+ (/ w 2) (* r (Math/sin (Math/toRadians t))))
            :y (+ (/ h 2) (* r (Math/cos (Math/toRadians t))))}
        p2 {:x (+ (/ w 2) (* r (Math/sin (Math/toRadians (+ 120 t)))))
            :y (+ (/ h 2) (* r (Math/cos (Math/toRadians (+ 120 t)))))}
        p3 {:x (+ (/ w 2) (* r (Math/sin (Math/toRadians (+ 240 t)))))
            :y (+ (/ h 2) (* r (Math/cos (Math/toRadians (+ 240 t)))))}]
    (doseq [x (range 4)
            y (range 6)]
      (.drawImage g image (* x 128) (* y 128) nil))
    (doto g
      (.setColor (Color/white))
      (.drawLine (:x p1) (:y p1) (:x p2) (:y p2))
      (.drawLine (:x p2) (:y p2) (:x p3) (:y p3))
      (.drawLine (:x p3) (:y p3) (:x p1) (:y p1))
      (.drawString (format "%.2f" @filtered-time) 10 20))))

(defn main-panel []
  (let [the-panel (proxy [JPanel] [] (paint [g] (render g this)))]
    (.setPreferredSize the-panel (Dimension. (* 128 4) (* 128 6)))
    the-panel))

(defn tick [elapsed]
  (dosync (alter theta + (/ elapsed 500))))

(defn go []
  (let [frame (JFrame. "fps-test2")
        panel (main-panel)]
    (doto frame (.add panel) .pack .show)
    (loop [old-time (get-time 0)]
      (when (.isShowing frame)
        (let [elapsed (- (get-time 0) old-time)]
          (if (> elapsed 15)
            (do (.repaint panel)
                (recur (get-time 0)))
            (do (tick elapsed)
                (Thread/sleep 0 1)
                (recur old-time))))))))

(go)
