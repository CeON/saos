/**
 * Generates url params for the given json paramObject (key - value pairs). Omits params with empty values.
 * 
 * <br/><br/>
 * Example:<br/> <br/>
 * paramObject =>
 * 
 * courtCriteria: {
 *    courtType: "COMMON",
 *    scCourtChamberId: "",
 *    ccCourtId: 12;
 * }
 * 
 * <br/>
 * paramPrefix => courtFilter
 * 
 * <br/><br/>
 * Results in =>
 * courtFilter.courtType=COMMON&courtFilter.ccCourtId=12
 *  
 * <br/><br/>
 * <b>Note</b>: paramObject has to be a simple flat json object. Nested objects and arrays are treated as values, for example:
 * <br/> <br/>
 * this: <br/>
 * var json = {};
 * <br/> 
 * json.criteria = {aaa : 'bbb'};
 * <br/>
 * console.log(generateUrlParams(json));
 *<br/><br/>
 * will give:
 * <br/>
 * criteria=%5Bobject%20Object%5D
 *  
 * @param paramObject object from which the url params will be generated
 * @param paramPrefix prefix that will be added to any of the param, prefix will be followed by dot (.)
 * 
 * 
 * 
 */
function generateUrlParams(paramObject, paramPrefix) {
    return Object.keys(paramObject)
        .filter(function(key) {
                return paramObject[key];
        })
        .map(function(key) {
            var urlParam = encodeURIComponent(key) + '=' + encodeURIComponent(paramObject[key]);
            if (paramPrefix) {
                urlParam = paramPrefix+"."+urlParam;
            }
            return urlParam;
        })
        .join('&');

    }



/**
 * Returns true if a point defined by posPageX and posPageY is inside an element with id equal to
 * elementId.
 * @param elementId of the element on the html page, e.g. a div
 * @param posPageX absolute x page position of the point  
 * @param posPageY absolute y page position of the point 
 * @returns {Boolean}
 */
function isInsideElement(elementId, posPageX, posPageY) {
    var $element = $("#"+elementId);
       
    var elementOffset = $element.offset();
    var elementLeft = elementOffset.left;
    var elementTop = elementOffset.top;
    var elementRight = elementLeft + $element.width();
    var elementBottom = elementTop + $element.height();
        
    if (posPageX >= elementLeft && posPageX <= elementRight &&
       posPageY >= elementTop && posPageY <= elementBottom) {
       return true;
    }
        
    return false;
}



/**
 * Inits & binds two complementary sections - one that is an info and the second that is
 * a form part that a user can use to change the info. In general when the info section is clicked then
 * it is hidden and the form section is shown to allow changing of the data. And then if
 * the user clicks outside the form section, the form section disappears and the info section
 * with updated data is shown again.
 * @param infoSectionId id of the info section element 
 * @param infoSectionId id of the form section element
 * @param updateInfoSectionAction the action that updates the info section with data from form section
 * 
 */
function initInfoFormSections(infoSectionId, formSectionId, updateInfoSectionAction) {
        
    updateInfoSectionAction();
        
    var formSection = $('#' + formSectionId);
    var infoSection = $('#' + infoSectionId);

    infoSection.click(function() {
        infoSection.hide();
        formSection.show();

    });
    
    $(document).off('mouseup', hideFormSectionIfClickedOutside);    
    $(document).on('mouseup', null, [formSection, infoSection, updateInfoSectionAction], hideFormSectionIfClickedOutside);
}

/** 
 * Hides a form section and shows an info section if the target of the click is outside
 * the form section. <br/> 
 * Assumes e parameter holds a formSection, infoSection and updateInfoSectionAction variables 
 * (see #initInfoFormSections for description) in e.data[0], e.data[1], e.data[2] respectively.
 * */
function hideFormSectionIfClickedOutside(e) {
    
    var formSection = e.data[0];
    var infoSection = e.data[1];
    var updateInfoSectionAction = e.data[2];
    
    if (!formSection.is(e.target) && !infoSection.is(e.target) // if the target of the click isn't the container...
            && formSection.has(e.target).length === 0 && infoSection.has(e.target).length === 0) // ... nor a descendant of the container
    {
        formSection.hide();
        infoSection.show();
        updateInfoSectionAction();
    }

    
}

function clearFieldsInContainer(container) {
    container.find("input, select, textarea").each(function() {
        clearField($(this));
    });
}

function clearField(field) {
    
    if (field.is("input:checkbox")) {
        field.prop("checked", false);
    } else if (field.is("input:radio")) {
        field.prop("checked", false);
        if ($("body").find('input:radio[name="' + field.attr("name") + '"]').first().is(field) ) {
            field.prop("checked", true).trigger("click");
        }
    } else if (field.is("input")) {
        field.val("")
            .trigger("change");
    } else if (field.is("select")) {
        field.removeAttr('selected')
            .val("")
            .trigger("change");
    } else if (field.is("textarea")) {
        field.val("")
            .trigger("change");
    }
}

    
function jAlert(messageText) {
	$("<span class='col-md-7'>" + messageText +" </span>").dialog({ modal: true, width: "500px", buttons: [ { text: "Ok", click: function() { $( this ).dialog( "close" ); } } ] });
}

function clearSubmitMade() {
    __submitMade = false;
}


/**
 * Extracts and returns an index of the passed element. Assumes that the index is the last part
 * of the element id preceded by an underscore (_). <br/>
 * This function is useful in situation where one prints collection elements and then wants to deal with them individually
 * (for example delete from collection).
 */   
function extractIndex(element) {
    return element.attr('id').split('_')[1];
}



/**
 * Is the given number int? 
 */
function isInt(n) {
    return n % 1 === 0;
 }


/****************** MONTH YEAR RANGE PICKER **/


/**
 * Ties the month-year range selects. Month-year range selects are four html selects 
 * corresponding to month start, year start, month end, year end. Options of these selects are whole numbers
 * representing months or years. </br>
 * This function binds the selects together so, for example, changes to one of them can influence the other ones.
 * E.g. changing the end year to the value lower than the start year decreases the start year.
 * 
 * @param startMonthId the id of the select representing the range start month
 * @param startYearId the id of the select representing the range start year
 * @param endMonthId the id of the select representing the range end month
 * @param endYearId the id of the select representing the range end year
 */
function tieMonthYearRangeSelects(startMonthId, startYearId, endMonthId, endYearId) {
    
    var startMonth = $('#' + startMonthId);
    var startYear = $('#' + startYearId);
    var endMonth = $('#' + endMonthId);
    var endYear = $('#' + endYearId);
    
    
    startYear.change(function() {
        
        if (+startYear.val() >= +endYear.val()) {
            
            endYear.val(startYear.val());
            
            if (+startMonth.val() > +endMonth.val()) {
                
                endMonth.val(startMonth.val());
                
            }
        }
        
    });


    endYear.change(function() {
        
        if (+startYear.val() >= +endYear.val()) {
            
            startYear.val(endYear.val());
            
            if (+startMonth.val() > +endMonth.val()) {
                
                startMonth.val(endMonth.val());
                
            }
        }
        
    });
    
    
    startMonth.change(function() {
       
        if (+startYear.val() == +endYear.val() && +startMonth.val() > +endMonth.val()) {
            
            endMonth.val(startMonth.val());
            
        }
            
    });
    

    endMonth.change(function() {
        
        if (+startYear.val() == +endYear.val() && +startMonth.val() > +endMonth.val()) {
            
            startMonth.val(endMonth.val());
            
        }
            
    });
    
    
}


/**
 * Formats the given number by invoking #addSpacesEvery3Digits(value) and by
 * fixing it to the given precision (if the value is not integer)
 * @param value the number to format
 * @param decimalPrecision the precision the number should be fixed to
 */
function formatNumber(value, decimalPrecision) {
    
    var precision = 0;
    if (!isInt(value)) {
        precision = decimalPrecision;
    }

    return addSpacesEvery3Digits(value.toFixed(precision));
}

/**
 * Adds space every three digits
 * 
 * @param nStr number in string format 
 * */
function addSpacesEvery3Digits(nStr) {
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ' ' + '$2');
	}
	return x1 + x2;
}



$(document).ready(function() {
	
    //configure locale language for moment
    moment.locale(springMessage.lang, {
        months : springMessage.months,
        monthsShort: springMessage.monthsShort,
        longDateFormat : {
            LL : "D MMMM YYYY"
        },
    });
    
    
	clearSubmitMade();
	
	$.blockUI.defaults.css = { border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' };
	$.blockUI.defaults.message = '<img src="${contextPath}/static/images/ajax-loader.gif">';
    
	
    /* Enable bootstrap tooltip */
    $('[data-toggle="tooltip"]').tooltip({container: 'body'});
	
    /* Enable bootstrap popover's */
    $('[data-toggle="popover"]').popover({container: 'body'});
    
    
    /* Hints */
    $(".hint").click(function(event) {
    	event.preventDefault();
    });
	
	/* Anchor with empty href, should not reload page*/
	$("a[href='']").click(function(event) {
		event.preventDefault();
	});
	
    //Navigation menu 
	$("#menu-button").click(function() {
	    var $menu = $("#nav-menu"),
	        menuHidePosition = "-" + ($menu.outerWidth()+1) + "px";
	    
	    if ($menu.css("left") === "0px") {
	        $menu.animate({left: menuHidePosition}, 400, function() {
	            $menu.css("visibility", "hidden");
	        });
	    } else if ($menu.css("left") === menuHidePosition) {
	        $menu.css("visibility", "visible");
	        $menu.animate({left: "0px"}, 400);
	    }
	});
	
    
    ConvertFakeEmailAddressToReal({
        addressTag: ".dummy-mail",
        
        itemsToReplace: [
            {
                fake: "_malpka_",
                real: "@"
            },
            {
                fake: "_kropka_",
                real: "."
            }
        ]
    });
    
    CookiePolicy.init({
        buttonShowMessageId: "#cookie-show-message",
        buttonPreviewAcceptId: "#cookie-preview-accept",
        buttonAcceptId: "#cookie-accept",
        messageId: "#cookie-message",
        windowId: "#cookie-window"
    });
    
});
