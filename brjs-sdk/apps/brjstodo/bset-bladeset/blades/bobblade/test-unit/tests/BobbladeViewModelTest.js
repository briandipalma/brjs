(function(){
	'use strict';
	
	var BobbladeViewModelTest = TestCase( 'BobbladeViewModelTest' );
	
	var BobbladeViewModel = require( 'brjstodo/bset/bobblade/BobbladeViewModel' );
	
	BobbladeViewModelTest.prototype.testSomething = function() {
	  var model = new BobbladeViewModel();
	  assertEquals( 'Welcome to your new Blade.', model.welcomeMessage() );
	};
}());