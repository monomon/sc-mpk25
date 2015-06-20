(
SynthDef(\mpkKeys, {
	arg freq, velocity, inBus=0, outBus=0;

	var env = EnvGen.kr(
		Env.adsr,
		velocity,
		velocity/2,
		doneAction: 2
	);

	var osc = Mix.ar([
		SinOsc.ar(
			[freq/2, freq],
			0,
			[env*0.7, env]
		),
		Saw.ar(
			[freq, freq*1.05],
			[env, env*0.7]
		),
		PinkNoise.ar(env*0.8)
	]);

	var synth = BPF.ar(
		osc,
		freq + BrownNoise.kr(freq/2) + SinOsc.kr(2.3, 0, freq/2),
		1.1,
	);
	Out.ar(outBus, synth!2);
}).add;

SynthDef(\mpkBass, {
	arg freq, velocity, inBus=0, outBus=0;

	var env = EnvGen.kr(
		Env.perc(0.005, 1, 1, -5),
		velocity,
		velocity/2,
		doneAction: 2
	);

	var osc = Mix.ar([
		Pulse.ar(
			[freq/2, freq], // subbass
			0.5,
			[env*0.7, env]
		),
		Saw.ar(freq, env),
	]);

	// resonance filter around the frequency
	// with lfo of 2.7 (make this controlled by wheel)
	var filtered = Resonz.ar(
		osc,
		freq + SinOsc.kr(2.7, 0.5,freq/3),
		0.9
	);

	var synth = LPF.ar(
		filtered,
		freq+20
	);
	Out.ar(outBus, synth!2);
}).add;


SynthDef(\mpkBass2, {
	arg freq, velocity, inBus=0, outBus=0;

	var env = EnvGen.kr(
		Env.perc(0.005, 2, 1, -5),
		velocity,
		velocity/2,
		doneAction: 2
	);

	var osc = Mix.ar([
		Pulse.ar(
			freq,
			0.5,
			env*0.7
		),
		SinOsc.ar(
			freq,
			0,
			env*0.8
		)
	]);

	var filtered = Resonz.ar(
		osc,
		freq + SinOsc.kr(2.7, 0.5,freq/3),
		0.9
	);

	var synth = LPF.ar(
		filtered*0.8,
		freq+20
	);
	Out.ar(outBus, synth!2);
}).add;

SynthDef(\massiveBass, {
	arg freq, velocity, inBus=0, outBus=0;
	var env = EnvGen.kr(
		Env.adsr(0.001, 0.5,0.8, 0.05, 0.9),
		velocity,
		velocity/2,
		doneAction: 2
	);

	//TODO give an envelope to the PWM
	var osc = Mix.ar([
		Pulse.ar(
			[freq/2, freq],
			SinOsc.kr(2.6, 0.5, 0.5) + 0.5,
			[env*0.5, env]
		),
		Pulse.ar(
			(freq.cpsmidi + 12).midicps,
			0.5,
			env
		),
		Saw.ar(
			(freq.cpsmidi + 19).midicps,
			env*0.85
		)
	]);

	var filtered = Resonz.ar(
		osc,
		freq + SinOsc.kr(1.8, 0.5,freq/2) + 20,
		1.5
	);

	var chorused = DelayC.ar(
		filtered,
		0.3,
		0.1,
		env
	);

	var synth = LPF.ar(
		Mix.ar([filtered, chorused*0.7]),
		SinOsc.kr(0.1, 0, 100, 200)
	);
	velocity.debug;
	Out.ar(outBus, synth!2);
}).add;

SynthDef(\anotherBass, {
	arg freq, velocity, inBus=0, outBus=0;
	var env = EnvGen.kr(
		Env.adsr(0.001, 0.5,0.9, 0.05, 0.95),
		velocity,
		velocity/2,
		doneAction: 2
	);

	//TODO give an envelope to the PWM
	var osc = Mix.ar([
		Pulse.ar(
			[freq/2, freq],
			SinOsc.kr(2.6, 0.5, 0.5) + 0.5,
			[env*0.5, env]
		),
		Pulse.ar(
			(freq.cpsmidi + 12).midicps,
			SinOsc.kr(2.6, 0.5, 0.5) + 0.5,
			env*0.8
		),
		Saw.ar(
			(freq.cpsmidi + 19).midicps,
			env*0.5
		)
	]);

	var filtered = Resonz.ar(
		osc,
		freq + SinOsc.kr(1.8, 0.5,freq) + 20,
		1.5
	);

	var chorused = DelayC.ar(
		filtered,
		0.3,
		0.1,
		env
	);

	var synth = LPF.ar(
		Mix.ar([filtered, chorused*0.7]),
		SinOsc.kr(0.1, 0, 100, 300)
	);
	velocity.debug;
	Out.ar(outBus, synth!2);
}).add;

SynthDef(\rising, {
	arg freq, velocity, inBus=0, outBus=0;

	var freq_env = EnvGen.kr(
		Env.new(
			[freq, freq*5, freq*4],
			[10, 1],
			'lin'
		),
		1
	);

	var env = EnvGen.kr(
		Env.adsr(0.2, 0.4, 0.95),
		velocity,
		velocity,
		doneAction: 2
	);
	var synth = Mix.ar([
		Saw.ar(freq_env, env*0.5),
		SinOsc.ar(freq_env, 0, env*0.7)
	]);

	Out.ar(outBus, synth*0.5!2);
}).add;

SynthDef(\distorted, {
	arg freq, velocity, inBus=0, outBus=0;

	var env = EnvGen.kr(
		Env.adsr(0.3, 0.5, 0.7, 0.5),
		velocity,
		velocity/4,
		doneAction: 2
	);

	var synth = Mix.ar([
		Formant.ar(
			[freq/2, freq, freq*1.36],
			freq,
			freq/4,
			[env, SinOsc.kr(0.2,0,0.5)*env*0.7, SinOsc.kr(0.3,0,0.5)*env*0.4]
		).softclip(0.4)
	]);

	Out.ar(outBus, synth!2);
}).add;

// fx synth used on the fx bux after all sounds
SynthDef(\fx, {
	arg inBus=0, outBus=0;

	var reverbed = FreeVerb.ar(
		In.ar(inBus, 2),
		0.1,
		0.3,
		0.7
	);
	var in = In.ar(inBus, 2);
	var compressed = Compander.ar(
		in,
		in,
		0.6,
		1.05,
		0.05,
		0.03,
		0.08
	);

	Out.ar(outBus, compressed);
}).add;
)