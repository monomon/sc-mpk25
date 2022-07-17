(
// init everything
// TODO: move this to a class
~mpk = Dictionary[
	\midiports -> [],
	\midihandlers -> [],
	\synths -> [],
	\busses -> Dictionary[
		\audio -> [],
		\control -> []
	]
];

~init = {
	// this is required to register to midi system
	MIDIClient.init;

	// find ports to connect together
	~mpk[\midiports].add(
		MIDIIn.findPort("Akai MPK25", "Akai MPK25 MIDI 1")
	);

	// now connect them
	~mpk[\midiports].do({
		arg item, i;
		i.postln;
		MIDIIn.connect(i, item);
	});

	// add midi handlers
	~mpk[\midihandlers].add(
		MIDIFunc.noteOn({
			arg ...args;
			args.postln;
		}, srcID: ~mpk[\midiports][0].uid)
	);
};

~cleanup = {
	~mpk[\midihandlers].postln;
	~mpk[\midihandlers].do({
		arg item, i;
		item.free;
	});

	~mpk[\midiports].do({
		arg item, i;
		MIDIIn.disconnect(i, item);
	});

	MIDIClient.disposeClient;
};

)
