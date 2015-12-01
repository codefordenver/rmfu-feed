(ns rmfu-ui.about
  (:require [rmfu-ui.nav :refer [nav]]))

(defn about []
  [:div
   [nav]
   [:div.container.jumbotron.large-main
    [:div.row
     [:div.col-lg-12
      [:h1.text-center "About Feed"]
      [:p "Feed is collaboration between Rocky Mountain Farmers Union and Code for Denver to help provide a place for active engagement within RMFU's communities"]
      [:h2 "Rocky Mountain Farmers Union"]
      [:p "Rocky Mountain Farmers Union is an advocate for family farmers and ranchers, local communities, and consumers. We are a progressive grassroots organization whose members determine our priorities. Founded in 1907, Rocky Mountain Farmers Union represent farm and ranch families in Colorado, New Mexico and Wyoming. Working together with similar state chapters across America, we are the heart and soul of the National Farmers Union."]
      [:h2 "Code for Denver"]
      [:p "We believe technology and innovation are powerful tools we can all use to make our community an even more spectacular place to live.

We are a diverse group of volunteers who are passionate about leveraging the power of technology to benefit the people of our Denver community.

We tap into the potential of technology to tackle issues like food systems, food security, economic development, safety and justice.

We recognize that we are a part of a much larger movement to address some of the challenges that face our planet. In order to collectively have the biggest impact, we work in partnership and collaboration with both local government, and the international network of Code for America brigades of which we are a part.

We also team up with local nonprofits and other active community members to make sure we understand the issues, to brainstorm technology-based solutions, and to actualize our ideas in ways that aim for measurable community benefit."]]]]])
