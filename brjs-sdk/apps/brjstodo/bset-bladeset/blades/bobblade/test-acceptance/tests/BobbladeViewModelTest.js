(function(){
	'use strict';
	
	require( 'jasmine' );
	
	var originalConsoleLog = console.log;
	
	var BobbladeViewModel = require( 'brjstodo/bset/bobblade/BobbladeViewModel' );
	
	describe('Bobblade Tests', function() {
	
		beforeEach(function() {
			console.log = jasmine.createSpy("console.log");
		});
	
		afterEach(function() {
			console.log = originalConsoleLog;
		});
	
		it( 'Should log hello on load', function() {
			new BobbladeViewModel();
			expect(console.log).toHaveBeenCalledWith('Welcome to your new Blade.');
		});
	
	});
}());