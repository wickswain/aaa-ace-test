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
    now = new Date();
    year = now.getFullYear();

    for (i = 0; i < 4; i++) {
        $('.cruise_year')[0].options[i] = new Option(year + i, year + i);
    };
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

function showMulti(num) {
    var next = num + 1;
    $('.rem_flight' + num).hide();
    $('.add_flight' + num).hide();
    $('.air_flight_' + next).slideDown();
}

function hideMulti(num) {
    var prev = num - 1;
    $('.air_flight_' + num).slideUp();
    $('.rem_flight' + prev).show();
    $('.add_flight' + prev).show();
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
    $(dropdown).text(function() {
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
            return travelers + " Adult, " + cabin
        } else if (children == "0" && adults > "1") {
            return travelers + " Adults, " + cabin
        } else if (children == "1" && adults == "0") {
            return travelers + " Traveler, " + cabin
        } else {
            return travelers + " Travelers, " + cabin
        }

    });
}
