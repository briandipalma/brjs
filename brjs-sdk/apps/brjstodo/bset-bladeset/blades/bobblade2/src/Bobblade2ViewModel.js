'use strict';

var ko = require( 'ko' );
var i18n = require( 'br/I18n' );

function Bobblade2ViewModel() {
	this.eventHub = require('service!br.event-hub');
	this.welcomeMessage = ko.observable( 'Welcome to your new Blade.' );
	this.buttonClickMessage = ko.observable( i18n( 'brjstodo.bset.bobblade2.button.click.message' ) );
	this.logWelcome();
}

Bobblade2ViewModel.prototype.buttonClicked = function() {
	console.log( 'button clicked' );
	var channel = this.eventHub.channel('bobblade2-channel');
	channel.trigger( 'hello-event', { some: 'Hello World!' } );
};

Bobblade2ViewModel.prototype.logWelcome = function() {
	console.log(  this.welcomeMessage() );
}

module.exports = Bobblade2ViewModel;
