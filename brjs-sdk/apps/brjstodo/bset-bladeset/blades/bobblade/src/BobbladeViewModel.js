'use strict';

var ko = require( 'ko' );
var i18n = require( 'br/I18n' );
//var r = require('brjstodo/bset/bobblade2/Bobblade2ViewModel');
function BobbladeViewModel() {
	this.eventHub = require('service!br.event-hub');
	this.welcomeMessage = ko.observable( 'Welcome to your new Blade.' );
	this.buttonClickMessage = ko.observable( i18n( 'brjstodo.bset.bobblade.button.click.message' ) );
	this.logWelcome();
}

BobbladeViewModel.prototype.buttonClicked = function() {
	console.log( 'button clicked' );
	var channel = this.eventHub.channel('bobblade-channel');
	channel.trigger( 'hello-event', { some: 'Hello World!' } );
};

BobbladeViewModel.prototype.logWelcome = function() {
	console.log(  this.welcomeMessage() );
}

module.exports = BobbladeViewModel;
