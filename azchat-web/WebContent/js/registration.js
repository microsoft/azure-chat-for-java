/**
 * This js file contains function of typeahead to Auto populate Country List while typing.
 */
$(document).ready(function() {
	   $("#errorDiv_Ajax").empty();
		//Country Code Validation
		var isCountryCode=isInteger($("#input_countryCD").val());
		//If country code if present then only convert to the name for user else keep as it is.
		if(isCountryCode==true){
			$("#input_countryCD").val(getCountryByCode($("#input_countryCD").val()).countryName);
		}
	
		$("#input_countryCD").typeahead({
			source : function(query, process) {
				return 	process(getCountryNames());
			},  //  TYPEAHEAD SOURCE :   END
		    matcher: function (item) {
		        if (item.toLowerCase().indexOf(this.query.trim().toLowerCase()) != -1) {
		            return true;
		        }
		    },
		    sorter: function (items) {
		        return items.sort();
		    } ,
		    highlighter: function (item) {
		        var regex = new RegExp( '(' + this.query + ')', 'gi' );
		        return item.replace( regex, "<strong>$1</strong>" );
		    },
		    updater: function (item) {
		    	$("#dupCountryCD").val(getCountryByName(item).countryCD);
		        return item;
		    }
		});
		
		$("[type=file]").on("change", function(){
			  var file = this.files[0].name;		 
			  var dflt = $(this).attr("placeholder");
			  $('label[for="avatar"]').text(file);
			  if($(this).val()!=""){
			    $(this).next().text(file);
			  } else {
			    $(this).next().text(dflt);
			  }
		});
		
		/**
		 *  Capture the onClick event on registration page submit button click.
		 */
		$("#btn_submt_reg").click(function(event){
			$("#errorDiv_Ajax").empty();
			$(".errorDiv").empty();
			var countryCD=$("#dupCountryCD").val();
			var countryName=getCountryByName($("#input_countryCD").val());
			if((countryName==null || countryName=='') || (countryCD==null || countryCD=='')){
				$("#errorDiv_Ajax").html("<font size='1' color='red'>Country selection is not valid.Please select valid country from drop down.</font>");
				return false;
			}
		});
		
		
});