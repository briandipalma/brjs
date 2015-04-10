(function(){
	'use strict';
	
	require( 'jasmine' );
	
	var originalConsoleLog = console.log;
	
	var Bobblade2ViewModel = require( 'brjstodo/bset/bobblade2/Bobblade2ViewModel' );
	
	describe('Bobblade2 Tests', function() {
	
		beforeEach(function() {
			console.log = jasmine.createSpy("console.log");
		});
	
		afterEach(function() {
			console.log = originalConsoleLog;
		});
	
		it( 'Should log hello on load', function() {
			new Bobblade2ViewModel();
			expect(console.log).toHaveBeenCalledWith('Welcome to your new Blade.');
		});
	
	});
}());