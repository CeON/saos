/*
 * Cookie policy message
 * 
 * @author Łukasz Pawełczak
 */

var CookiePolicy = (function() {
    
    var scope = {},
        defaultOptions = {
            buttonShowMessageId: "#cookie-show-message",
            buttonPreviewAcceptId: "#cookie-preview-accept",
            buttonAcceptId: "#cookie-accept",
            messageId: "#cookie-message",
            windowId: "#cookie-window"
        },
        
        options = {},
        
        cookieName = "cookiePolicyAccepted",
        cookieTime = 2 * 365 * 24 * 60; // 2 years
    
    
    /*
     * Initializes module
     * 
     * @param userOptions - object 
     * 
     * {
     *     buttonShowMessageId: "", - Id of show message button located in cookie window.
     *     buttonPreviewAcceptId: "" - Id of button accept policy located in cookie window.  
     *     buttonAcceptId: "", - Id of button accept policy located in cookie message.
     *     messageId: "", - cookie message id
     *     windowId: "" - cookie window
     * }
     */
    scope.init = function(userOptions) {
        
        options = userOptions || defaultOptions;
        
        initButtonShowCookiePolicy();
        initButtonAcceptCookiePolicy();
        
    };
    
    return scope;
    
    
    /*
     * Shows cookie policy message
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
     * Creates cookie & hide message 
     */
    function initButtonAcceptCookiePolicy() {
        
        // Accept cookie policy
        $(options.buttonAcceptId).click(function() {

            $(options.messageId).slideUp(function() {
                saveCookie(true);
            });
        });
        
        $(options.buttonPreviewAcceptId).click(function() {
            
            saveCookie(true);
            
            //Hide cookie window
            $(options.windowId).fadeOut();
        });
        
    }
    
    
    function saveCookie(value) {
        Cookies.create(cookieName, value, cookieTime);
    }
    
})();


