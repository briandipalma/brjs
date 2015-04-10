'use strict';

var ko = require( 'ko' );

var ServiceRegistry = require( 'br/ServiceRegistry' );
//var InputViewModel = require('brjstodo/input/InputViewModel');
//var PresenterThing = require('brjstodo/PresenterThing');
//var r = require('brjstodo/bset/bobblade/BobbladeViewModel');
function ItemsViewModel() {
	//InputViewModel = require('brjstodo/input/InputViewModel');

	//PresenterThing();
	//debugger;
	this.todos = ko.observableArray( [
		{ title: 'foo' },
		{ title: 'bar' }
	] );

	this._eventHub = ServiceRegistry.getService( 'br.event-hub' );

	// register to recieve events
	this._eventHub.channel( 'todo-list' ).on( 'todo-added', this._todoAdded, this );
}

ItemsViewModel.prototype._todoAdded = function( added ) {
	this.todos.push( added );
};

module.exports = ItemsViewModel;
