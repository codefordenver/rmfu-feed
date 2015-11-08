(ns rmfu-ui.createarticle)

(defn createarticle []
  [:div.container.jumbotron.largemain
 [:div.row
  [:div.col-lg-12
   [:h1.text-center
    "Create Article"]
   [:h4
    "Title of Article"]
   [:input.form-control
    {:type "text", :placeholder "Milk contains up to 10% of the following hormones upon bottling.", :value ""}]
   [:br]
   [:h4
    "Link to Article (optional):"]
   [:input.form-control
    {:type "text", :placeholder "http://www.milksafe.com/hormones.html", :value ""}]
   [:br]
   [:h4
    "Description of Article (optional):"]
   [:textarea.form-control
    {:rows "4"}
    "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis auteirure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident\n          "]
   [:br]
   [:h4
    "Relevant to these Regions"]
  [:div.input-group
    [:input.form-control
      {:type "text", :placeholder "Enter Zip Code", :value ""}]
      [:span.input-group-addon.select-group
        [:select.c-select.select-item
         [:option
          "+1 mile"]
         [:option
          "+10 miles"]
         [:option
          "+50 miles"]]]
    [:div.input-group-btn
     [:button.btn.btn-default
      {:name "type", :type "button", :value "add"}
      "Add"]]]
   [:br]
   [:h4
    "Relevant to these Topics"]
   [:div.input-group
    [:input.form-control
     {:type "topics", :placeholder "Enter Keywords or select from suggestions below", :value ""}]
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
     {:name "type", :type "button", :value "create_article"}
     "Create the Article"]]]]])
