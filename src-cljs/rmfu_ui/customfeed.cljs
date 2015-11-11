(ns rmfu-ui.customfeed
  (:require [reagent.core :refer [atom]]
            [rmfu-ui.nav :refer [nav]]))

(defn input-range-component
  [state param descriptiontext]
  [:div.row
   [:p.bullet.col-lg-7
    descriptiontext]
   [:p.col-lg-5
    "Agree "
    [:input
     {:type      "range"
      :min       0
      :max       3
      :step      1
      :list      "agreevalues"
      :value     (:agreeness @state)                        ;; <---- notice the binding of the value to the input
      :on-change (fn [e]
                   (println (-> e .-target .-value))        ;; <----- checkout out the console,
                   ;; we should see this value printed
                   (swap! state assoc param (-> e .-target .-value)) ;; <--- state transition happening here!
                   )}]
    " Disagree"]])

(defn customfeed []
  (let [app-state1 (atom {:agreeness 0})
        app-state2 (atom {:agreeness 0})
        app-state3 (atom {:agreeness 0})
        app-state4 (atom {:agreeness 0})
        ]
    [:div
     [nav "customfeed"]
     [:div.container.jumbotron.largemain
      [:div.row
       [:div.col-lg-12
        [:h1
         "Custom Feed"]
        [:div.dategroup
         [:h4
          "New Legislation that will impact you! "]
         [:p.date
          "October 1, 2015"]]
        [:div.borderbox
         "\n                Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute\n              irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n          "]
        [:br]
        [:ul.list-inline
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Collapse"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Topics"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Comments"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Share"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Archive"]]]
        [:ul.list-inline
         [:li
          [:a
           {:href "#"}
           "Fertilizer"]]
         [:li
          [:a
           {:href "#"}
           "Legislation"]]
         [:li
          [:a
           {:href "#"}
           "Vegetables"]]
         [:li
          [:a
           {:href "#"}
           "Grain"]]
         [:li
          [:a
           {:href "#"}
           "Fruit"]]
         [:li
          "80246 +5 Miles"]]
        [:hr]
        [:div.dategroup
         [:h4.articleheading
          "Please help us serve you better"]
         [:p.date
          "September 20th, 2015"]]
        [input-range-component app-state1 :agreeness "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."]
        [input-range-component app-state2 :agreeness "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris."]
        [input-range-component app-state3 :agreeness "Duis aute irure dolor in pariatur."]
        [input-range-component app-state4 :agreeness "Excepteur sint occaecat deserunt mollit anim id est laborum."]
        [:ul.list-inline
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Submit"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Topics"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Comments"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Share"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Archive"]]]
        [:ul.list-inline
         [:li
          [:a
           {:href "#"}
           "Polls"]]
         [:li
          [:a
           {:href "#"}
           "Legislation"]]
         [:li
          [:a
           {:href "#"}
           "Youth"]]
         [:li
          [:a
           {:href "#"}
           "Funding"]]
         [:li
          "80246 +5 Miles"]]
        [:hr]
        [:div.dategroup
         [:h4.articleheading
          "Review the latest proposal"]
         [:p.date
          " September 1st, 2015"]]
        [:div.row
         [:div.col-lg-4
          [:p.document
           "New Legislation Proposal Draft.doc"]]
         [:div.col-lg-8
          [:ul.commentlist
           [:li
            "Joe - Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididul"]
           [:li
            "Bob - Consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et!"]
           [:li
            "Joe - Adipisicing elit?"]]]]
        [:ul.list-inline
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Download"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Topics"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Comments"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Share"]]
         [:li
          [:button.btn.btn-default
           {:type "button", :value "add"}
           "Archive"]]]
        [:ul.list-inline
         [:li
          [:a
           {:href "#"}
           "Documents"]]
         [:li
          [:a
           {:href "#"}
           "Legislation"]]
         [:li
          [:a
           {:href "#"}
           "Vegetables"]]
         [:li
          [:a
           {:href "#"}
           "Fruit"]]
         [:li
          "80246 +5 Miles"]]]]

      [:datalist#agreevalues
       [:option
        "Strongly Agree"]
       [:option
        "Agree"]
       [:option
        "Disagree"]
       [:option
        "Strongly Disagree"]]]]))
