'use strict';

require( 'jasmine' );

var ItemsViewModel = require( 'brjstodo/items/ItemsViewModel' );

var ServiceRegistry = require( 'br/ServiceRegistry' );

var fakeEventHub;
var fakeChannel;

describe('Items Tests', function() {

	beforeEach( function() {
		fakeChannel = {
			on: function(eventName, callback, context) {
				// store event name and data
				this.eventName = eventName;
				this.callback = callback;
				this.context = context;
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
	} );

	it( 'should register for todo events on the EventHub', function() {
		var itemsViewModel = new ItemsViewModel();

		expect( fakeEventHub.channelName ).toEqual( 'todo-list' );
		expect( fakeChannel.eventName ).toEqual( 'todo-added' );
		expect( fakeChannel.context ).toEqual( itemsViewModel );
	} );

	it( 'should add a new item when todo-added is triggered', function() {
	  var itemsViewModel = new ItemsViewModel();
	  var itemText = 'hello';

	  // trigger the callback
	  fakeChannel.callback.call( fakeChannel.context, { title: itemText } );

	  // check the item has been added to the end of the list
	  var items = itemsViewModel.todos();
	  expect( items[ items.length - 1 ].title ).toEqual( itemText );
	} );

} );
