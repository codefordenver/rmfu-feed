(ns rmfu-ui.customfeed)

(defn customfeed []
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
   [:div.row
    [:p.bullet.col-lg-7
     "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."]
    [:p.col-lg-5
     "Agree "
     [:input
      {:type "range", :min "0", :max "3", :value "0", :step "1", :list "agreevalues"}]
     " Disagree"]]
   [:div.row
    [:p.bullet.col-lg-7
     "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris."]
    [:p.col-lg-5
     "Agree "
     [:input
      {:type "range", :min "0", :max "3", :value "0", :step "1", :list "agreevalues"}]
     " Disagree"]]
   [:div.row
    [:p.bullet.col-lg-7
     "Duis aute irure dolor in pariatur."]
    [:p.col-lg-5
     "Agree "
     [:input
      {:type "range", :min "0", :max "3", :value "0", :step "1", :list "agreevalues"}]
     " Disagree"]]
   [:div.row
    [:p.bullet.col-lg-7
     "Excepteur sint occaecat deserunt mollit anim id est laborum."]
    [:p.col-lg-5
     "Agree "
     [:input
      {:type "range", :min "0", :max "3", :value "0", :step "1", :list "agreevalues"}]
     " Disagree"]]
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
       "Strongly Disagree"]]
     ]
)
