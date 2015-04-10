'use strict';

require( 'jasmine' );

var InputViewModel = require( 'brjstodo/input/InputViewModel' );

var ServiceRegistry = require( 'br/ServiceRegistry' );

var fakeEventHub;
var fakeChannel;

describe('Input Tests', function() {

	beforeEach(function() {

		fakeChannel = {
			trigger: function( eventName, data ) {
				// store event name and data
				this.eventName = eventName;
				this.data = data;
			}
		};

		fakeEventHub = {
			channel: function( channelName ) {
				// store the name of the channel
				this.channelName = channelName;
				return fakeChannel;
			}
		};

		// ensure there isn't already an event-hub registered
		ServiceRegistry.deregisterService( 'br.event-hub' );

		// Register the fake event hub
		ServiceRegistry.registerService( 'br.event-hub', fakeEventHub );

	});

	it( 'should trigger an event on EventHub when Enter is pressed', function() {
		// Initialize
		var testTodoTextValue = 'write some code and test it';
		var inputViewModel = new InputViewModel();
		inputViewModel.todoText( testTodoTextValue );

		// Execute test
		inputViewModel.keyPressed( null, { keyCode: 13 } );

		// Verify
		expect( fakeEventHub.channelName ).toEqual( 'todo-list' );
		expect( fakeChannel.eventName ).toEqual( 'todo-added' );
		expect( fakeChannel.data.title ).toEqual( testTodoTextValue );
	} );

});
