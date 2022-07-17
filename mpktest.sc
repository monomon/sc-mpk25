(
~fxBus = Bus.audio(s,2);
~drumFx = Synth.new(
	\drumFx,
	[
		\inBus, ~fxBus,
		\outBus, 0
	]
);

~mpk = MPK.new(
	~fxBus,
	// synth for the keyboard
	\massiveBass,
	// mappings from note to synth for drumpads
	Dictionary[
				36 -> \bassdrum,
				37 -> \snare1,
				38 -> \snare2,
				39 -> \clap,
				40 -> \hat1,
				41 -> \hat2
			]
);
~mpk.keyboard.handlers.postln;
~mpk.keyboard.midiport.postln;
~mpk.drumpads.handlers.postln;
)

(
~mpk.keyboard.synthdefs = \weird;
)

(
~mpk.clean;
)