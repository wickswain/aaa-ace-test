var interval = setInterval(function() {
    var showHideMembershipCols = function() {
        var numberOfRows = $(this).attr('data-rowcount');
        var fixedRows = $(this).attr('data-defaultrowcount');
        if (numberOfRows > fixedRows) {
            var showCount = parseInt(fixedRows) * 2;
            if (showCount != 0) {
                showCount = parseInt(showCount) - 1;
                $(this).find('.membership-cols:gt(' + showCount + ')').hide();
            } else {
                $(this).find('.membership-cols').hide();
            }
            $(this).find(".seeAllLink").show();
        } else {
            $(this).find(".seeAllLink").remove();
        }
    };
    $(".membership-comparison").each(function() {
        showHideMembershipCols.call(this);
    });
    if (document.readyState === 'complete') {
        // Membership comparison script starts
        $(".membership-row-text, .membership-row-img, .icon-info").find("div").css("padding", "0px").css("border", "none").css("display", "inherit");
        $(".membership-row-text, .membership-row-img, .icon-info").find("span").css("padding", "0px").css("border", "none");
        $(".seeAll").click(function() {
            $(this).parent().parent().parent().find('.membership-cols').css("display", "flex");
            $(this).parent().parent().remove();
        });
        // Membership comparison ends
        // Travel Widget script starts
        // Variables declaration starts
        var club_number = $('#club_number').val();
        var ref_id = $('#ref_id').val();
        if (typeof ref_id === 'undefined') {
            var requestURL = window.location.hostname;
            if (requestURL.split('.').length > 2) {
                var regionName = requestURL.split('.')[1];
                ref_id = getReferenceID(regionName);
            }
        }
        var searchbox_options = {
            refid: ref_id,
            open_window: true,
            environment: "prod",
            hotel: {
                elements: {
                    form: "hotel",
                    autosuggest: ".rs_autosuggest",
                    rooms: ".rs_rooms",
                    search: ".rs_search",
                    chk_in: ".rs_chk_in, .rs_chk_in_calendar, .rs_date_chk_in",
                    chk_out: ".rs_chk_out, .rs_chk_out_calendar, .rs_date_chk_out",
                    chk_in_display: ".rs_date_chk_in",
                    chk_out_display: ".rs_date_chk_out"
                },
                calendar: {
                    output_format: "<div class='rs_mobi_chk_day'>[d]</div><div class='rs_mobi_chk_month'>[F]</div>",
                    next_day: false
                },
                select_name: true
            },
            car: {
                elements: {
                    form: "car",
                    from: ".rs_pickup",
                    to: ".rs_dropoff",
                    chk_in: ".rs_chk_in, .rs_chk_in_calendar, .rs_date_chk_in",
                    chk_out: ".rs_chk_out, .rs_chk_out_calendar, .rs_date_chk_out",
                    chk_in_display: ".rs_date_chk_in",
                    chk_out_display: ".rs_date_chk_out",
                    search: ".rs_search",
                    different_return: "#rs_different"
                },
                calendar: {
                    output_format: "<div class='rs_mobi_chk_day'>[d]</div><div class='rs_mobi_chk_month'>[F]</div>",
                    next_day: false
                },
                select_name: true
            },
            air: {
                enabled: true,
                elements: {
                    form: 'air',
                    round_trip: '#air_round_trip',
                    one_way: '#air_one_way',
                    multi_dest: '#air_multi_dest',
                    adults: '.rs_adults_input',
                    children: '.rs_child_input',
                    chk_in: '.rs_chk_in, .rs_mobi_in',
                    chk_out: '.rs_chk_out, .rs_mobi_out',
                    chk_in_display: '.rs_mobiin',
                    search: ".rs_search"
                },
                calendar: {
                    output_format: '<div class="rs_mobi_chk_day">[d]</div><div class="rs_mobi_chk_month">[F]</div>',
                    next_day: false
                },
                autosuggest: {
                    airports: true
                },
                select_name: true
            },
            vp: {
                active: true,
                elements: {
                    chk_in: ".rs_chk_in, .rs_chk_in_calendar, .rs_date_chk_in",
                    chk_out: ".rs_chk_out, .rs_chk_out_calendar, .rs_date_chk_out",
                    chk_in_display: ".rs_date_chk_in",
                    chk_out_display: ".rs_date_chk_out",
                    search: ".rs_search"
                },
                calendar: {
                    output_format: "<div class='rs_mobi_chk_day'>[d]</div><div class='rs_mobi_chk_month'>[F]</div>",
                    next_day: false
                },
                select_name: true
            },
            mvp: {
                elements: {
                    chk_in: ".rs_chk_in, .rs_chk_in_calendar, .rs_date_chk_in",
                    chk_out: ".rs_chk_out, .rs_chk_out_calendar, .rs_date_chk_out",
                    chk_in_display: ".rs_date_chk_in",
                    chk_out_display: ".rs_date_chk_out"
                },
                calendar: {
                    output_format: "<div class='rs_mobi_chk_day'>[d]</div><div class='rs_mobi_chk_month'>[F]</div>"
                },
                select_name: false
            }
        };
        //Variables declaration end
        $.getScript('https://secure.rezserver.com/public/js/searchbox/searchbox.min.js', function() {
            if ($('.travel-widget').length > 0) {
                $('input[name=rs_adults]').val("1");
                $('input[name=rs_children]').val("0");
                $('.dropbtn').text('1 Adult, Economy');
                $(".rs_tabs").on("click", function(e) {
                    var futureTab = $(this).data("tab"),
                        $selectedForm = $("." + futureTab);
                    if (futureTab === 'rs_mvp_form') {
                        var mvp_start = $('.rs_chk_in').val();
                        var mvp_end = $('.rs_chk_out').val();
                        var mvp_kw = $('#mvp-keywords').val();
                        var url = $(".rs_mvp_form").attr('action');
                        var scrollToInternal = $('#scrollToInternal').val();
                        var scrollvacationsID = $('#scrollvacationsID').val();
                        var scrollId = '#' + scrollvacationsID;
                        var openInNewtab = $("#vcOpeninNewTabflag").val();
                        if (scrollToInternal == "true") {
                            $('html,body').stop().animate({
                                scrollTop: $(scrollId).offset().top
                            }, 500, 'swing');
                            e.preventDefault();
                        } else if (url !== '#') {
                            if (url.match("^/content")) {
                                url = url + ".html";
                            }
                            if (openInNewtab == "true") {
                                window.open(url, '_blank');
                            } else {
                                window.open(url, '_self');
                            }
                        }
                    } else {
                        if ($selectedForm.hasClass("rs_searchbox_hide")) {
                            $selectedForm.removeClass('rs_searchbox_hide').siblings("form").addClass("rs_searchbox_hide");
                        }
                    }
                });
                $(".rs_more_option").on("click", function() {
                    var futureTab = $(this).data("tab"),
                        $selectedForm = $("." + futureTab);
                    if ($selectedForm.hasClass("rs_searchbox_hide")) {
                        $selectedForm.removeClass('rs_searchbox_hide').siblings("form").addClass("rs_searchbox_hide");
                    }
                });
                $("#tabAir").on("click", function() {
                    $("#flight_only").addClass("rs_option_highlight");
                    $("#flight_vp").removeClass('rs_option_highlight');
                });
                $('#round-drop').click(function(e) {
                    e.stopPropagation();
                    if ($('#round-content').css('display') == 'none') {
                        $("#round-content").show();
                        $(".dropbtn").addClass('upArrow');
                    } else {
                        $("#round-content").hide();
                        $(".dropbtn").removeClass('upArrow');
                    }
                });
                $(window).click(function() {
                    $("#round-content").hide();
                    $(".dropbtn").removeClass('upArrow');
                });
                $('#round-content').click(function(e) {
                    e.stopPropagation();
                });
                $('#one-drop').click(function(e) {
                    e.stopPropagation();
                    if ($('#one-content').css('display') == 'none') {
                        $("#one-content").show();
                        $(".dropbtn").addClass('upArrow');
                    } else {
                        $("#one-content").hide();
                        $(".dropbtn").removeClass('upArrow');
                    }
                });
                $(window).click(function() {
                    $("#one-content").hide();
                    $(".dropbtn").removeClass('upArrow');
                });
                $('#one-content').click(function(e) {
                    e.stopPropagation();
                });
                $(".cabin_select").on("mousedown", function(e) {
                    e.preventDefault();
                    this.blur();
                    window.focus();
                });
                $(".prevClass").on("click", function(e) {
                    e.preventDefault();
                    var menu = $(this).parents().eq(2).children().eq(1).attr('id');
                    if ($(this).siblings('select').children('option:selected').prev().length == "0") {
                        $(this).siblings('select').children('option').last().prop('selected', true);
                    } else {
                        $(this).siblings('select').children('option:selected').prev().prop('selected', true);
                    }
                    updateTravelers(menu);
                });
                $(".nextClass").on("click", function(e) {
                    e.preventDefault();
                    var menu = $(this).parents().eq(2).children().eq(1).attr('id');
                    if ($(this).siblings('select').children('option:selected').next().length == "0") {
                        $(this).siblings('select').children('option').first().prop('selected', true);
                    } else {
                        $(this).siblings('select').children('option:selected').next().prop('selected', true);
                    }
                    updateTravelers(menu);
                });
                $(".rs_adults_input").focus(function() {
                    $(this).blur();
                });
                $('.plus_button').unbind().click(function() {
                    var menu = $(this).parents().eq(4).attr('id');
                    var number = $(this).parent().siblings('input');
                    var value = number.val();
                    if (value < 8) {
                        value++;
                        number.val(value);
                    }
                    updateTravelers(menu);
                });
                $('.min_button').unbind().click(function() {
                    var menu = $(this).parents().eq(4).attr('id');
                    var number = $(this).parent().siblings('input');
                    var value = number.val();
                    if (value > 0) {
                        value--;
                        number.val(value);
                    }
                    updateTravelers(menu);
                });
                $('.rs_hotel_flight').click(function(event) {
                    event.preventDefault();
                    event.stopPropagation();
                    event.stopImmediatePropagation();
                    ga('send', 'event', 'Pleasant', 'click', 'Flight + Hotel');
                    window.open('http://www.aaa.com/scripts/WebObjects.dll/AAAOnline?association=aaa&club=240&page=PartnerRedirect&PID=403&http://www.pleasantholidays.com/deeplink?p_type=FH&g1a=2&rf=&d1=&f1=', '_blank');
                    return false;
                });
                generateCruiseDates();
                populateYears();
                populateMonth();
                if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
                    $("<link/>", {
                        rel: "stylesheet",
                        type: "text/css",
                        href: "css/mobile_search.css"
                    }).appendTo("head");
                }
                $('#different_return').click(function() {
                    $(".rs_dropoff_div").slideToggle();
                });
                $('.round-trip').click(function() {
                    $('#air_round_trip').show();
                    $('#air_one_way').hide();
                    $('#air_multi_dest').hide();
                });
                $('.one-way').click(function() {
                    $('#air_round_trip').hide();
                    $('#air_one_way').show();
                    $('#air_multi_dest').hide();
                });
                $('.multi-city').click(function() {
                    $('#air_round_trip').hide();
                    $('#air_one_way').hide();
                    $('#air_multi_dest').show();
                });
                var $icons = $('.rs_tabs');
                $icons.click(function() {
                    var futureTab = $(this).data("tab");
                    if (futureTab !== 'rs_mvp_form') {
                        $icons.removeClass('highlight_tab');
                        $(this).addClass('highlight_tab');
                    }
                });
                var $hotel_options = $('.rs_hotel_option');
                $hotel_options.click(function() {
                    $hotel_options.removeClass('rs_option_highlight');
                    $(this).addClass('rs_option_highlight');
                });
                var $air_options = $('.rs_air_option');
                $air_options.click(function() {
                    $air_options.removeClass('rs_option_highlight');
                    $(this).addClass('rs_option_highlight');
                });
                var $car_locations = $('.rs_car_option');
                $car_locations.click(function() {
                    $car_locations.removeClass('rs_car_highlight');
                    $(this).addClass('rs_car_highlight');
                });
                var $air_types = $('.rs_air_type');
                $air_types.click(function() {
                    $air_types.removeClass('rs_air_highlight');
                    $(this).addClass('rs_air_highlight');
                });
                $('.rs_tabs').click(function() {
                    $("#hotel_options").hide();
                    $("#air_options").hide();
                });
                $('#tabHotel').click(function() {
                    $("#hotel_options").show();
                });
                $('#tabAir').click(function() {
                    $("#air_options").show();
                });
            }
            $('#rs_multi').searchbox(searchbox_options);
            $('input[name="rad"]').click(function() {
                var $radio = $(this);
                if ($radio.data('waschecked') == true) {
                    $radio.prop('checked', false);
                    $radio.data('waschecked', false);
                } else {
                    $radio.data('waschecked', true);
                }
                $radio.siblings('input[name="rs_hotel_options"]').data('waschecked', false);
            });
            $('.rad-area').hide();
            $('.rs_chk_out').change(function(e) {
                $('.rs_chk_out').val($(this).val());
                e.preventDefault();
            });
            $('.rs_chk_in').change(function(e) {
                $('.rs_chk_in').val($(this).val());
                e.preventDefault();
            });
            $(document).on('mouseover', '.rs_chk_in_box .rs_cal_table .rs_cal_day, .rs_chk_in1_box .rs_cal_table .rs_cal_day', function() {
                $('.rs_chk_in').val('');
            });
            $(document).on('mouseover', '.rs_chk_out_box .rs_cal_table .rs_cal_day', function() {
                $('.rs_chk_out').val('');
            });
            $(document).on('click', '.rs_cal_table .rs_cal_day', function() {
                $('.rs_chk_out_box').css('display', 'none');
            });
            $('#mvp-keywords').click(function() {
                if ($(this).val() == "Destination / Keyword") {
                    $(this).val('');
                }
            });
            $(".rs_mvp_form").submit(function(e) {
                var mvp_start = $('.rs_chk_in').val();
                var mvp_end = $('.rs_chk_out').val();
                var mvp_kw = $('#mvp-keywords').val();
                var action = $(".rs_mvp_form").attr('action');
                var openInNewtab = $("#vcOpeninNewTabflag").val();
                if (mvp_kw == "Destination / Keyword") {
                    mvp_kw = "";
                }
                if (mvp_start == "mm/dd/yyyy") {
                    mvp_start = "";
                }
                if (mvp_end == "mm/dd/yyyy") {
                    mvp_end = "";
                }
                var url = action + "?keyword=" + mvp_kw + "&start=" + mvp_start + "&end=" + mvp_end + "";
                if (openInNewtab) {
                    var win = window.open(url, '_blank');
                    win.focus();
                } else {
                    window.location.href = url;
                }
                return false;
            });
        });
        // Travel widget scripts end
        /**
         * Card Carousel
         */
        $('.carousel-showmanymoveone .item').each(function() {
            var itemToClone = $(this);
            for (var i = 1; i < 4; i++) {
                itemToClone = itemToClone.next();
                // wrap around if at end of item collection
                if (!itemToClone.length) {
                    itemToClone = $(this).siblings(':first');
                }
                // grab item, clone, add marker class, add to collection
                itemToClone.children(':first-child').clone().addClass("cloneditem-" + (i)).appendTo($(this));
            }
        });
        // clear the interval after the document loaded completely.
        clearInterval(interval);
    }
}, 100);