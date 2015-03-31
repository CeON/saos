/*
 * Cookie policy message
 * 
 * @author Łukasz Pawełczak
 */

var CookiePolicy = (function() {
    
    var scope = {},
        defaultOptions = {
            buttonShowMessageId: "#cookie-show-message",
            buttonAcceptId: "#cookie-accept",
            messageId: "#cookie-message",
            windowId: "#cookie-window"
        },
        
        options = {},
        
        cookieName = "cookiePolicyAccepted",
        cookieTime = 7 * 24 * 60; // 7 days
    
    
    scope.init = function(arguments) {
        
        options = arguments || defaultOptions;
        
        initButtonShowCookiePolicy();
        initButtonAcceptCookiePolicy();
        
    };
    
    return scope;
    
    
    /*
     * Show cookie policy message
     */
    function initButtonShowCookiePolicy() {
        
        $(options.buttonShowMessageId).click(function() {
            
            //Hide cookie window
            $(options.windowId).fadeOut();
            
            //Show cookie policy 
            $(options.messageId).slideDown();
        });

    }
    
    /*
     * Create cookie & hide message 
     */
    function initButtonAcceptCookiePolicy() {
        
        // Accept cookie policy
        $(options.buttonAcceptId).click(function() {

            $(options.messageId).slideUp(function() {
                saveCookie(true);
            });
        });
        
    }
    
    
    function saveCookie(value) {
        Cookies.create(cookieName, value, cookieTime);
    }
    
})();


