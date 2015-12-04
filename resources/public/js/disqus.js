/* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
var disqus_shortname = 'rmfu-feed'; // required: replace example with your forum shortname
/* * * DON'T EDIT BELOW THIS LINE * * */
(function () {
    var dsq = document.createElement('script');
    dsq.type = 'text/javascript';
    dsq.async = true;
    dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
    (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
})();
window.disqusReset = function (identifier, url) {
    console.log('disqusReset');
    console.log(identifier);
    console.log(url);
    if (DISQUS) {
        DISQUS.reset({
            reload: true,
            config: function () {
                this.page.identifier = identifier;
                this.page.url = url;
            }
        });
    }
};