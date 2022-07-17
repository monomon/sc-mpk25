/*
 * Interactive usage for mpk
 */
Platform.userExtensionDir;
s.queryAllNodes;
MIDIClient.init;
/*
 startup
*/
(
// fx bus
~fxBus = Bus.audio(s, 2);

// init MPK
~mpk = MPK.new([
	outBus: ~fxBus
]);

// receive from fx bus
~fx = Synth.new(
	\fx,
	[ \inBus, ~fxBus],
	~mpk.keyboard.synthGroup,
	\addAfter
);
)
(
~mpk.clean;
~fx.free;
)
~mpk.keyboard.synths = nil;
// switch synths
// TODO: think of a better system - UI?
~mpk.keyboard.synthdef = \mpkBass;
~mpk.keyboard.synthdef = \mpkBass2;
~mpk.keyboard.synthdef = \mpkKeys;
~mpk.keyboard.synthdef = \massiveBass;
~mpk.keyboard.synthdef = \anotherBass;
~mpk.keyboard.synthdef = \noiseCrap;
~mpk.keyboard.synthdef = \machineNoise;
~mpk.keyboard.synthdef = \rising;
~mpk.keyboard.synthdef = \distorted;
~mpk.keyboard.synthdef = \bar;
~mpk.keyboard.handlers;
~mpk.drumpads.midiport;
// route mpk busses to effects busses, output
MIDIFunc.allEnabled;
MIDIFunc.trace(true);
MIDIClient.sources;
