$(document).ready(function() {
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
});