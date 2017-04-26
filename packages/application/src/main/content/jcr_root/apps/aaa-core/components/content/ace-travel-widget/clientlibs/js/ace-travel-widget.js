

    // var MY_RESERVATIONS = "http://www.aaa.com/MyTrips";
    // var BASE_URL = "http://secure.rezserver.com/";
    // var END_URL = "/home/?refid=";
    var PH_URL = "http://www.pleasantholidays.com/deeplink?Partner_OAPartnerOID=";
    var FLIGHT_MULTI_CITY_URL = "https://secure.rezserver.com/flights/home/?refid=";

    var _phPartnerCode = "partnerCode__.__";
    var _phWebSerialNumber = "__..__webSerialNumber__.__";

    var _currentClub;

    var CLUBS = [
        {club: "alabama", clubCode: "001", refID: "5755", partnerCode: "1817", webSerial: "1116"},
        {club: "autoclubmo", clubCode: "065", refID: "5783", partnerCode: "21533", webSerial: "1183"},
        {club: "california", clubCode: "004", refID: "5733", partnerCode: "53709", webSerial: "1001"},
        {club: "eastcentral", clubCode: "215", refID: "5770", partnerCode: "31333", webSerial: "1198"},
        {club: "hawaii", clubCode: "018", refID: "5738", partnerCode: "60996", webSerial: "1482"},
        {club: "newmexico", clubCode: "061", refID: "5748", partnerCode: "58593", webSerial: "1281"},
        {club: "northernnewengland", clubCode: "036", refID: "5741", partnerCode: "67113", webSerial: "2732"},
        {club: "texas", clubCode: "252", refID: "5799", partnerCode: "58592", webSerial: "1280"},
        {club: "tidewater", clubCode: "258", refID: "5780", partnerCode: "35190", webSerial: "1167"}];

    ////handle preloader
    $(".rs_rooms").bind("DOMNodeInserted", function () {
        ///ensure dropdown has populated
        if ($(".rs_rooms").children().length > 5) {
            $('.rs_rooms').unbind("DOMNodeInserted");
            $(".rs_hotel_form").removeClass("hidden");
            $(".widget-preloader").remove();

            $(".rs_tabs").removeClass("rs_tab_preload");
        }

    });

    function buildWidgetData() {
        var _hostName = window.location.hostname;
        _hostName = _hostName.split(".");

        ///prod or local
        var _clubIndex = (_hostName.length !== 1) ? 1 : 0;

        $.each(CLUBS, function (i, data) {
            if (_hostName[_clubIndex] === data.club) {
                _currentClub = data;
            }
        });

        ///build PH URL
        _currentClub.phURL = PH_URL + _phPartnerCode + _currentClub.partnerCode + _phWebSerialNumber + _currentClub.webSerial;

        ///partner data
        $(".hotel_flight_hotel").attr("href", _currentClub.phURL);
        $(".multi-link").find("a").attr("href", FLIGHT_MULTI_CITY_URL + _currentClub.refID);
        $("input[name='ref_id']").attr("value", _currentClub.refID);
        $("input[name='club_number']").attr("value", _currentClub.clubCode);
        $("input[name='club']").attr("value", _currentClub.clubCode);


        var searchbox_options = {
            refid: _currentClub.refID,
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

        ////flight and hotel advanced searches
        $(".advanced_search_link").on("click touchstart", function () {
            var _advancedSearchOptions = ($("form[name='hotel']").hasClass("rs_searchbox_hide")) ? $(".rs_flight_advanced_options") : $(".hotels_advanced_search");
            if ($(_advancedSearchOptions).hasClass("hidden")) {
                $(_advancedSearchOptions).removeClass("hidden");
                $(".advanced_search_link").text("Hide advanced search");
            } else {
                $(_advancedSearchOptions).addClass("hidden");
                $(".advanced_search_link").text("Advanced search");
            }
        });



        ///hertz promo toggle
        $(".hertz_promo").on("click touchstart", function () {
            if ($(".rs_car_advanced_search").hasClass("hidden")) {
                $(".rs_car_advanced_search").removeClass("hidden");
                $(".hertz_promo").text("Hide Hertz promo code");
            } else {
                $(".rs_car_advanced_search").addClass("hidden");
                $(".hertz_promo").text("Hertz promo code");
            }
        });

        $("#rs_coupon_number").on("input", function () {
            if (($(this).val().length > 6) || (isNaN($(this).val()))) {
                alert('Invalid promo code.  Hertz promo codes are 1-6 characters and only numbers.');
            }
        });

        $("#rs_car_discount_PP").on("input", function () {
            if (($(this).val().length > 8) || (isNaN($(this).val()))) {
                alert('Invalid Hertz Gold Rewards code.  Hertz Gold Rewards codes are 8 characters and only numbers.');
            }
        });


        $(document).ready(function(){
            $.getScript('https://secure.rezserver.com/public/js/searchbox/searchbox.min.js', function(data, textStatus){
                if(textStatus === "success"){

                    $('input[name=rs_adults]').val("1");
                    $('input[name=rs_children]').val("0");
                    $('.dropbtn').text('1 adult, Economy');
                    $(".rs_tabs").on("click", function (e) {
                        var futureTab = $(this).data("tab"),
                            $selectedForm = $("." + futureTab);

                        if ($selectedForm.hasClass("rs_searchbox_hide")) {
                            $selectedForm.removeClass('rs_searchbox_hide').siblings("form").addClass("rs_searchbox_hide");
                        }

                        ///reset advanced searches
                        $(".rs_flight_advanced_options").addClass("hidden");
                        $(".hotels_advanced_search").addClass("hidden");
                        $(".rs_car_advanced_search").addClass("hidden");
                        $(".advanced_search_link").text("Advanced search");
                        $(".hertz_promo").text("Hertz promo code");
                    });

                    $("#tabAir").on("click", function () {
                        $("#flight_only").addClass("rs_option_highlight");
                        $("#flight_vp").removeClass('rs_option_highlight');
                    });
                    $('#round-drop').click(function (e) {
                        e.stopPropagation();
                        if ($('#round-content').css('display') == 'none') {
                            $("#round-content").show();
                            $(".dropbtn").addClass('upArrow');
                        } else {
                            $("#round-content").hide();
                            $(".dropbtn").removeClass('upArrow');
                        }
                    });
                    $(window).click(function () {
                        $("#round-content").hide();
                        $(".dropbtn").removeClass('upArrow');
                    });
                    $('#round-content').click(function (e) {
                        e.stopPropagation();
                    });
                    $('#one-drop').click(function (e) {
                        e.stopPropagation();
                        if ($('#one-content').css('display') == 'none') {
                            $("#one-content").show();
                            $(".dropbtn").addClass('upArrow');
                        } else {
                            $("#one-content").hide();
                            $(".dropbtn").removeClass('upArrow');
                        }
                    });
                    $(window).click(function () {
                        $("#one-content").hide();
                        $(".dropbtn").removeClass('upArrow');
                    });
                    $('#one-content').click(function (e) {
                        e.stopPropagation();
                    });
                    $(".cabin_select").on("mousedown", function (e) {
                        e.preventDefault();
                        this.blur();
                        window.focus();
                    });
                    $(".prevClass").on("click", function (e) {
                        e.preventDefault();
                        var menu = $(this).parents().eq(2).children().eq(1).attr('id');
                        if ($(this).siblings('select').children('option:selected').prev().length == "0") {
                            $(this).siblings('select').children('option').last().prop('selected', true);
                        } else {
                            $(this).siblings('select').children('option:selected').prev().prop('selected', true);
                        }
                        updateTravelers(menu);
                    });
                    $(".nextClass").on("click", function (e) {
                        e.preventDefault();
                        var menu = $(this).parents().eq(2).children().eq(1).attr('id');
                        if ($(this).siblings('select').children('option:selected').next().length == "0") {
                            $(this).siblings('select').children('option').first().prop('selected', true);
                        } else {
                            $(this).siblings('select').children('option:selected').next().prop('selected', true);
                        }
                        updateTravelers(menu);
                    });
                    $(".rs_adults_input").focus(function () {
                        $(this).blur();
                    });
                    $('.plus_button').unbind().click(function () {
                        var menu = $(this).parents().eq(4).attr('id');
                        var number = $(this).parent().siblings('input');
                        var value = number.val();
                        if (value < 8) {
                            value++;
                            number.val(value);
                        }
                        updateTravelers(menu);
                    });
                    $('.min_button').unbind().click(function () {
                        var menu = $(this).parents().eq(4).attr('id');
                        var number = $(this).parent().siblings('input');
                        var value = number.val();
                        if (value > 0) {
                            value--;
                            number.val(value);
                        }
                        updateTravelers(menu);
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
                    $('#different_return').click(function () {
                        $(".rs_dropoff_div").slideToggle();
                    });
                    $('.round-trip').click(function () {
                        $('#air_round_trip').show();
                        $('#air_one_way').hide();
                        $('#air_multi_dest').hide();
                    });
                    $('.one-way').click(function () {
                        $('#air_round_trip').hide();
                        $('#air_one_way').show();
                        $('#air_multi_dest').hide();
                    });
                    $('.multi-city').click(function () {
                        $('#air_round_trip').hide();
                        $('#air_one_way').hide();
                        $('#air_multi_dest').show();
                    });
                    var $icons = $('.rs_tabs');
                    $icons.click(function () {
                        var futureTab = $(this).data("tab");
                        if (futureTab !== 'rs_mvp_form') {
                            $icons.removeClass('highlight_tab');
                            $(this).addClass('highlight_tab');
                        }
                    });
                    var $hotel_options = $('.rs_hotel_option');
                    $hotel_options.click(function () {
                        $hotel_options.removeClass('rs_option_highlight');
                        $(this).addClass('rs_option_highlight');
                    });
                    var $air_options = $('.rs_air_option');
                    $air_options.click(function () {
                        $air_options.removeClass('rs_option_highlight');
                        $(this).addClass('rs_option_highlight');
                    });
                    var $car_locations = $('.rs_car_option');
                    $car_locations.click(function () {
                        $car_locations.removeClass('rs_car_highlight');
                        $(this).addClass('rs_car_highlight');
                    });
                    var $air_types = $('.rs_air_type');
                    $air_types.click(function () {
                        $air_types.removeClass('rs_air_highlight');
                        $(this).addClass('rs_air_highlight');
                    });
                    $('.rs_tabs').click(function () {
                        $("#hotel_options").hide();
                        $("#air_options").hide();
                    });
                    $('#tabHotel').click(function () {
                        $("#hotel_options").show();
                    });
                    $('#tabAir').click(function () {
                        $("#air_options").show();
                    });

                    $('#rs_multi').searchbox(searchbox_options);
                    $('input[name="rad"]').click(function () {
                        var $radio = $(this);
                        if ($radio.data('waschecked') == true) {
                            $radio.prop('checked', false);
                            $radio.data('waschecked', false);
                        } else {
                            $radio.data('waschecked', true);
                        }
                        $radio.siblings('input[name="rs_hotel_options"]').data('waschecked', false);
                    });

                    $('.rs_chk_out').change(function (e) {
                        $('.rs_chk_out').val($(this).val());
                        e.preventDefault();
                    });
                    $('.rs_chk_in').change(function (e) {
                        $('.rs_chk_in').val($(this).val());
                        e.preventDefault();
                    });
                    $(document).on('click', '.rs_cal_table .rs_cal_day', function () {
                        $('.rs_chk_out_box').css('display', 'none');
                    });

                    $(document).on('mouseover', '.rs_chk_in_box .rs_cal_table .rs_cal_day, .rs_chk_in1_box .rs_cal_table .rs_cal_day', function(){
                        $('.rs_chk_in').val('');
                    });
                    $(document).on('mouseover', '.rs_chk_out_box .rs_cal_table .rs_cal_day', function(){
                        $('.rs_chk_out').val('');
                    });
                }
            });
        });
    }


    function checkCruise(el) {
        var destination = $('select[name=destination]', el),
            cruiseline = $(
                'select[name=cruiseline]', el),
            date = $('select[name=date]', el);

        if (destination.val() == '') {
            alert('Please select a destination.');
            return false;
        }

        if (cruiseline.val() == '') {
            alert('Please select a cruise line.');
            return false;
        }

        if (date.val() == '') {
            alert('Please select a date.');
            return false;
        }

        return true;
    }

    function populateYears() {
        var now = new Date();
        var year = now.getFullYear();

        for (var i = 0; i < 4; i++) {
            $('.cruise_year')[0].options[i] = new Option(year + i, year + i);
        }
        ;
    }

    function populateMonth() {
        var now = new Date();
        var  month = now.getMonth();
        month = month + 1;
        $('.cruise_month').val(month);
    }

    function generateCruiseDates() {
        var result = '',
            now = new Date(),
            today = new Date(now.getFullYear(), now
                .getMonth(), now.getDate()),
            currentDate = today,
            maxDate = new Date(
                today.getFullYear(), today.getMonth() + 12, today.getDate()),
            monthNames = [
                'January', 'February', 'March', 'April', 'May', 'June', 'July',
                'August', 'September', 'October', 'November', 'December'
            ];

        while (currentDate < maxDate) {
            result += '<option value="' + (currentDate.getMonth() + 1) + 'x' +
                currentDate.getFullYear() + '">' +
                monthNames[currentDate.getMonth()] + ' ' +
                currentDate.getFullYear() + '</option>';
            currentDate = new Date(currentDate.getFullYear(), currentDate
                    .getMonth() + 1, currentDate.getDate());
        }

        $('.rs_cruise_form .rs_cruise_select').append(result);
    }

    function sameLocation() {
        $(".rs_dropoff_div").hide();
        $(".rs_pickup_div").removeClass('rs_car_half');
    }

    function differentLocation() {
        $(".rs_dropoff_div").show();
        $(".rs_pickup_div").addClass('rs_car_half');
    }

    function updateTravelers(menu) {
        var name = menu.split("-content");
        name.pop();
        var dropdown = "#" + name.toString() + "-drop",
            cabinClass = "#" +
                name.toString() + "-class";
        $(dropdown).text(function () {
            var adults = $(this).siblings('.dropdown-content')
                    .find('input[name=rs_adults]').val(),
                children = $(
                    this).siblings('.dropdown-content').find(
                    'input[name=rs_children]').val(),
                travelers = +adults +
                    +children,
                cabin = $(cabinClass).find(
                    ":selected").text();

            if (children == "0" && adults == "1") {
                return travelers + " adult, " + cabin
            } else if (children == "0" && adults > "1") {
                return travelers + " adults, " + cabin
            } else if (children == "1" && adults == "0") {
                return travelers + " traveler, " + cabin
            } else {
                return travelers + " travelers, " + cabin
            }

        });
    }

    buildWidgetData();