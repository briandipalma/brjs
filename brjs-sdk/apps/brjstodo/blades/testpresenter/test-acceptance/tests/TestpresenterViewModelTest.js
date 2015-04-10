(function(){
	'use strict';
	
	require( 'jasmine' );
	
	var originalConsoleLog = console.log;
	
	var TestpresenterViewModel = require( 'brjstodo/testpresenter/TestpresenterViewModel' );
	
	describe('Testpresenter Tests', function() {
	
		beforeEach(function() {
			console.log = jasmine.createSpy("console.log");
		});
	
		afterEach(function() {
			console.log = originalConsoleLog;
		});
	
		it( 'Should log hello on load', function() {
			new TestpresenterViewModel();
			expect(console.log).toHaveBeenCalledWith('Welcome to your new Blade.');
		});
	
	});
}());