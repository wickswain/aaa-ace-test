function getReferenceID(regionName) {
    if(regionName === 'calif') {
		return '5773';
    } else if(regionName === 'texas') {
		return '5799';
    } else if(regionName === 'newmexico') {
		return '5748';
    } else if(regionName === 'hawaii') {
		return '5738';
    } else if(regionName === 'northernnewengland') {
		return '5741';
    } else if(regionName === 'tidewater') {
		return '5780';
    } else if(regionName === 'alabama') {
		return '5755';
    } else if(regionName === 'autoclubmo') {
		return '5783';
    } else if(regionName === 'eastcentral') {
		return '5770';
    } else {
		return '';
    }
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
