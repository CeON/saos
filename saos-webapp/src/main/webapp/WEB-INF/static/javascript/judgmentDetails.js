/**
 * Modules used in judgment details view.
 * 
 * @author Łukasz Pawełczak
 */
var jsInitInJudgmentDetails = function(options) {

   
    //Corrections info section 
    $("#corrections-toggle").click(function() {
        var $section = $("#corrections-section"),
            $button = $(this),
            animationOptions = {
                time: 300,
                easing: "swing"
            };
        
        
        if ($section.css("display") === "none") {
            $button.addClass("info-section-button-open");
            $section.slideDown(animationOptions);
            $button.attr('aria-expanded', 'true');
        } else if ($section.css("display") === "block") {
            $button.removeClass("info-section-button-open");
            $section.slideUp(animationOptions);
            $button.attr('aria-expanded', 'false');
        }
    });
    
    //Sourceinfo info section 
    $("#source-info-toggle").click(function() {
        var $section = $("#source-info-section"),
            $button = $(this),
            animationOptions = {
                time: 300,
                easing: "swing"
            };
        
        
        if ($section.css("display") === "none") {
            $button.addClass("info-section-button-open");
            $section.slideDown(animationOptions);
            $button.attr('aria-expanded', 'true');
        } else if ($section.css("display") === "block") {
            $button.removeClass("info-section-button-open");
            $section.slideUp(animationOptions);
            $button.attr('aria-expanded', 'false');
        }
    });

}

