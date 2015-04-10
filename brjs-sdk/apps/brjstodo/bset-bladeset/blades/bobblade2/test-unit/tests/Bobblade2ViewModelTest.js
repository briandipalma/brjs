(function(){
	'use strict';
	
	var Bobblade2ViewModelTest = TestCase( 'Bobblade2ViewModelTest' );
	
	var Bobblade2ViewModel = require( 'brjstodo/bset/bobblade2/Bobblade2ViewModel' );
	
	Bobblade2ViewModelTest.prototype.testSomething = function() {
	  var model = new Bobblade2ViewModel();
	  assertEquals( 'Welcome to your new Blade.', model.welcomeMessage() );
	};
}());