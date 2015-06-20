// representing single control
// holding state like registered handlers, midiport, synths and busses
MPKControl {
	// a collection of MIDIFuncs for all message types
	var <handlers,
	midiport,
	// this is a dictionary mapping from note to synth instance
	// used to turn off a note if released while playing
	<synths,
	// all synths of this control are added to this group
	<synthGroup,
	// for the generic control this argument
	// is a single name (string or symbol) of a synthDef
	<>bsynthdefs,
	// output bus
	<outBus;

	*new {
		arg aMidiport, aMidiportNum, aOutBus, aSynthDefs;
		^super.new.init(aMidiport, aMidiportNum, aOutBus, aSynthDefs);
	}

	init {
		arg aMidiPort, midiportNum, aOutBus, aSynthDefs;

		var h;
		var s = Server.default;

		// find and connect midi port
		midiport = MIDIIn.findPort(aMidiPort[0], aMidiPort[1]);
		MIDIIn.connect(midiportNum, midiport);

		synthdefs = aSynthDefs;

		synthdefs.debug;
		// synths is a dictionary per note
		synths = Dictionary.new;
		// add synths to group for easier cleaning
		synthGroup = Group.new;

		if (midiport === nil, {
			Error("midiport % could not be opened, quitting...".format(aMidiPort)).throw;
		}, {
			"midiport % opened".format(midiport).debug;
		});

		// hook all handlers
		// they are a dict
		// msgType -> function
		// here create a MIDIFunc for each of them
		// and add it to a collection
		h = this.handlerDefs;
		// TODO: handlers might also be a Dictionary
		handlers = h.keys.collect({
			arg key;

			MIDIFunc(
				h[key],
				msgType: key,
				// TODO: for cc this would be nil?
				srcID: midiport.uid
			)
		});
	}

	/**
	 Handler definitions for all message types.
	 Map from message name to a handler function.
	 These can be overloaded in subclasses for different behavior.
	 The one implemented here creates a new synth on noteOn for the current note and adds it
	 to a dictionary with note number as the key.
	 This dictionary is used in noteOff to release the synth for the stopped note.
	 */
	handlerDefs {
		^Dictionary[
			\noteOn -> {
				arg val, num, chan;

				// trigger new synth on noteON
				// keep the note in a dict
				synths[num] = Synth.new(
					synthdefs.value,
					[
						\freq, num.midicps,
						\velocity, val/127.0,
						\outBus, outBus
					],
					synthGroup,
					\addToHead
				);
			},
			\noteOff -> {
				arg val, num, chan;

				// free up synth at this note
				synths[num].set(\velocity, val);
				synths.debug;
			},
			\bend -> {
				arg val, chan;
				"TODO: bend messages".debug;
				val.debug;
			},
			\control -> {
				arg val, num, chan;
				"TODO: control messages".debug;
				val.debug;
			}
		];
	}

	// clean up the mess
	clean {
		handlers.do({
			arg item, i;
			item.free;
		});
		outBus.free;
		synthGroup.free;
		"MPK control released".debug;
	}
}

MPKKeyboard : MPKControl {
}

MPKDrumpads : MPKControl {

	handlerDefs {
		^Dictionary[
			\noteOn -> {
				arg val, num, chan;

				num.debug;
				synthdefs[num].debug;

				// trigger new synth on noteON
				// keep the note in a dict
				synths[num] = Synth.new(
					synthdefs[num].value,
					[
						\velocity, val/127.0,
						\outBus, outBus
						],
					synthGroup,
					\addToHead
					);
			},
			\noteOff -> {
				arg val, num, chan;

				// do nothing on noteoff
			},
			\bend -> {
				arg val, chan;
				"TODO: bend messages".debug;
				val.debug;
			},
			\control -> {
				arg val, num, chan;
				"TODO: control messages".debug;
				num.debug;
				val.debug;
			}
		];
	}
}

/**
 high-level class representing MPK controller
 */
MPK {
	var <keyboard, <drumpads, <cc, <>outBus;

	*new {
		arg outBus, aSynthDef, aPadSynthDefs;
		^super.new.init(outBus, aSynthDef, aPadSynthDefs);
	}

	init {
		arg aOutBus, aSynthDef, aPadSynthDefs;

		outBus = aOutBus;

		'initializing MPK'.debug;
		MIDIClient.init;

		// TODO: might want to move midi port names to arguments
		// MPKControl.new( source psrt, destination port, audio output, synthdef )
		keyboard = MPKKeyboard.new(
			["Akai MPK25", "Akai MPK25 MIDI 1"],
			0,
			outBus,
			aSynthDef
		);

		// drumpads have a note number (indicating pad)
		// and velocity
		// drumpads - TODO
		drumpads = MPKDrumpads.new(
			["Akai MPK25", "Akai MPK25 MIDI 2"],
			1,
			outBus,
			aPadSynthDefs
		);
	}

	// clean up resources
	clean {
		keyboard.clean;
		drumpads.clean;
		"MPK cleaned".debug;
	}

}
