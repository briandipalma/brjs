(function(){
	'use strict';
	
	var TestpresenterViewModelTest = TestCase( 'TestpresenterViewModelTest' );
	
	var TestpresenterViewModel = require( 'brjstodo/testpresenter/TestpresenterViewModel' );
	
	TestpresenterViewModelTest.prototype.testSomething = function() {
	  var model = new TestpresenterViewModel();
	  assertEquals( 'Welcome to your new Blade.', model.welcomeMessage() );
	};
}());