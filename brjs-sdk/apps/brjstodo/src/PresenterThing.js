var br = require( 'br/Core' );
var PresentationModel = require('br/presenter/PresentationModel');
var EditableProperty = require('br/presenter/property/EditableProperty');

var NumericValidator = require('br/validation/NumericValidator');

var PresenterThing = function() {
	this.todoText = new EditableProperty(123);
	/*this.validatorInfo = {};
	this.todoText.addValidationErrorListener({listen:function(){console.log("verr")}}, "listen");
	var nv = new NumericValidator("Wrong input");
	this.todoText.addValidator(nv, null, this.validatorInfo);

	setTimeout(function(){
		console.log("remove validator");
		this.todoText.removeValidator(this.validatorInfo)
	}.bind(this), 5000);

	setTimeout(function(){
		console.log("add validator");
		var nv = new NumericValidator("Wrong input");
		this.todoText.addValidator(nv, null, this.validatorInfo);
	}.bind(this), 10000);

	setTimeout(function(){
		console.log("remove validator");
		this.todoText.removeValidator(this.validatorInfo)
	}.bind(this), 15000);*/
};
br.extend(PresenterThing, PresentationModel);
module.exports = PresenterThing;