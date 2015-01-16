// Static JSON Object code containing country list
var countryMap = '{ "countries" : ['
		+ '{ "countryName":"India" , "countryCD":"91" },'
		+ '{ "countryName":"Australia" , "countryCD":"44" },'
		+ '{ "countryName":"Canada" , "countryCD":"1" },'
		+ '{ "countryName":"China" , "countryCD":"86" },'
		+ '{ "countryName":"Singapore" , "countryCD":"65" },'
		+ '{ "countryName":"Switzerland" , "countryCD":"41" },'
		+ '{ "countryName":"United States(US)" , "countryCD":"1" },'
		+ '{ "countryName":"Argentina" , "countryCD":"54" },'
		+ '{ "countryName":"Austria" , "countryCD":"43" },'
		+ '{ "countryName":"Belgium" , "countryCD":"32" },'
		+ '{ "countryName":"Brazil" , "countryCD":"55" },'
		+ '{ "countryName":"Denmark" , "countryCD":"45" },'
		+ '{ "countryName":"Egypt" , "countryCD":"20" },'
		+ '{ "countryName":"France" , "countryCD":"33" },'
		+ '{ "countryName":"Germany" , "countryCD":"49" },'
		+ '{ "countryName":"Greece" , "countryCD":"30" },'
		+ '{ "countryName":"United Arab Emirates(UAE)" , "countryCD":"971" },'
		+ '{ "countryName":"Japan" , "countryCD":"81" },'
		+ '{ "countryName":"Russia" , "countryCD":"7" } ]}';

// Parse JSON string to the JSON Object
var countryMapJSON = JSON.parse(countryMap);
// Global Map for country
var countryMapByName = {}
var countryMapByCode = {}
var countryNames = [];
// Create the map
$.each(countryMapJSON.countries, function(index, country) {
	countryMapByCode[country.countryCD] = country;
	countryMapByName[country.countryName] = country;
	countryNames.push(country.countryName);
});

/**
 * This function returns the country object when provided with the country code.
 * 
 * @param code
 * @returns
 */
function getCountryByCode(code) {
	return countryMapByCode[code];
}

/**
 * This method returns the country object when provided with the country name.
 * 
 * @param name
 * @returns
 */
function getCountryByName(name) {
	return countryMapByName[name];
}
/**
 * get country name array.
 * 
 * @returns {Array}
 */
function getCountryNames() {
	return countryNames;
}
/**
 * This value returns if the passed value is integer.
 * 
 * @param n
 * @returns {Boolean}
 */
function isInteger(n) {
	var intRegex = /^-?[0-9]+$/;
	return intRegex.test(n);
}


