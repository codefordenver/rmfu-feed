(ns rmfu-ui.profile
  (:require [rmfu-ui.nav :refer [nav]]))

(defn profile []
  [:div
   [nav "profile"]
   [:div.container.jumbotron.largemain
    [:div.row
     [:div.col-lg-12
      [:h3.text-center
       "Tell Us About Yourself"]
      [:div.col-lg-6
       [:input.form-control.pull-left
        {:type "text", :placeholder "First Name", :value ""}]]
      [:div.col-lg-6
       [:input.form-control.pull-right
        {:type "text", :placeholder "Last Name", :value ""}]]
      [:br]
      [:br]
      [:div.dropdown.col-lg-3
       [:button#dropdownMenu1.btn.btn-secondary.dropdown-toggle
        {:type "button", :data-toggle "dropdown", :aria-haspopup "true", :aria-expanded "false"}
        "\n            Demographic\n          "]
       [:div.dropdown-menu
        [:a.dropdown-item
         {:href "#"}
         "Action"]
        [:a.dropdown-item
         {:href "#"}
         "Another action"]
        [:a.dropdown-item
         {:href "#"}
         "Something else here"]
        [:div.dropdown-divider]
        [:a.dropdown-item
         {:href "#"}
         "Separated link"]]]
      [:div.dropdown.col-lg-3
       [:button#dropdownMenu1.btn.btn-secondary.dropdown-toggle
        {:type "button", :data-toggle "dropdown", :aria-haspopup "true", :aria-expanded "false"}
        "\n            Demographic\n          "]
       [:div.dropdown-menu
        [:a.dropdown-item
         {:href "#"}
         "Action"]
        [:a.dropdown-item
         {:href "#"}
         "Another action"]
        [:a.dropdown-item
         {:href "#"}
         "Something else here"]
        [:div.dropdown-divider]
        [:a.dropdown-item
         {:href "#"}
         "Separated link"]]]
      [:div.dropdown.col-lg-3
       [:button#dropdownMenu1.btn.btn-secondary.dropdown-toggle
        {:type "button", :data-toggle "dropdown", :aria-haspopup "true", :aria-expanded "false"}
        "\n            Demographic\n          "]
       [:div.dropdown-menu
        [:a.dropdown-item
         {:href "#"}
         "Action"]
        [:a.dropdown-item
         {:href "#"}
         "Another action"]
        [:a.dropdown-item
         {:href "#"}
         "Something else here"]
        [:div.dropdown-divider]
        [:a.dropdown-item
         {:href "#"}
         "Separated link"]]]
      [:div.dropdown.col-lg-3
       [:button#dropdownMenu1.btn.btn-secondary.dropdown-toggle
        {:type "button", :data-toggle "dropdown", :aria-haspopup "true", :aria-expanded "false"}
        "\n            Demographic\n          "]
       [:div.dropdown-menu
        [:a.dropdown-item
         {:href "#"}
         "Action"]
        [:a.dropdown-item
         {:href "#"}
         "Another action"]
        [:a.dropdown-item
         {:href "#"}
         "Something else here"]
        [:div.dropdown-divider]
        [:a.dropdown-item
         {:href "#"}
         "Separated link"]]]
      [:div.col-lg-12 {:style {:height "20px"}}]
      [:h4
       "Regions of Interest"]
      [:div.input-group
       [:input.form-control
        {:type "text", :placeholder "Enter Zip Code", :value ""}]
       [:span.input-group-addon.select-group
        [:select.c-select.select-item
         [:option "+1 mile"]
         [:option "+10 miles"]
         [:option "+50 miles"]]]
       [:div.input-group-btn
        [:button.btn.btn-default
         {:name "type", :type "button", :value "add"}
         "Add"]]]
      [:ul.list-inline
       [:li
        "80246 +1 Miles"]
       [:li
        "80246 +10 Miles"]
       [:li
        "80123 +50 Miles"]]
      [:h4
       "Topics of Interest"]
      [:div.input-group
       [:input.form-control
        {:type "text", :placeholder "Enter Keywords or select from suggestions below", :value ""}]
       [:span.input-group-btn
        [:button.btn.btn-default
         {:type "button", :value "add"}
         "Add"]]]
      [:ul.list-inline
       [:li
        [:a
         {:href "#"}
         "Dairy"]]
       [:li
        [:a
         {:href "#"}
         "Organic"]]
       [:li
        [:a
         {:href "#"}
         "GMO"]]
       [:li
        [:a
         {:href "#"}
         "Fertilizer"]]
       [:li
        [:a
         {:href "#"}
         "Equipment"]]
       [:li
        [:a
         {:href "#"}
         "Poultry"]]
       [:li
        [:a
         {:href "#"}
         "Sustainable"]]
       [:li
        [:a
         {:href "#"}
         "Green"]]
       [:li
        [:a
         {:href "#"}
         "Pests"]]
       [:li
        [:a
         {:href "#"}
         "Tobacco"]]
       [:li
        [:a
         {:href "#"}
         "Fuel"]]
       [:li
        [:a
         {:href "#"}
         "Seeds"]]
       [:li
        [:a
         {:href "#"}
         "Wool"]]
       [:li
        [:a
         {:href "#"}
         "Solar"]]
       [:li
        [:a
         {:href "#"}
         "Wind"]]
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
        [:a
         {:href "#"}
         "Polls"]]]
      [:p.text-center
       [:button.btn
        {:name "type", :type "button", :value "continue"}
        "Continue to your custom feed"]]]]]])
