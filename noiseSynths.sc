(

SynthDef(\noiseCrap, {
	arg freq, velocity, inBus=0, outBus=0;

	var env = EnvGen.kr(
		Env.asr,
		velocity,
		velocity,
		doneAction: 2
	);

	var blipEnv = EnvGen.kr(
		Env.asr(0.3, 1, 1, -3),
		velocity,
		velocity/4,
		10,
		doneAction: 2
	);

	var noise = HPF.ar(
		RLPF.ar(
			Blip.ar(blipEnv, 20),
			freq,
			0.2,
			env
		),
		100
	);

	var delayed = DelayC.ar(
		noise
	);

	Out.ar(outBus, [noise, delayed*0.5]!2);
}).add;

SynthDef(\machineNoise, {
	arg freq, velocity, inBus=0, outBus=0;

	var env = EnvGen.kr(
		Env.asr,
		velocity,
		velocity,
		doneAction: 2
	);

	var blipEnv = EnvGen.kr(
		Env.asr(0.1, 1, 1, -3),
		velocity,
		freq/4,
		10,
		doneAction: 2
	);

	var noise = HPF.ar(
		RLPF.ar(
			Blip.ar(blipEnv, 20),
			freq,
			0.2,
			env,
		),
		150
	);

	var delayed = DelayC.ar(
		noise
	);

	Out.ar(outBus, [noise, delayed*0.5]!2);
}).add;

SynthDef(\bar, {
	arg freq, velocity, inBus, outBus=0;

	var env = EnvGen.kr(
		Env.perc(0.001, 2, 1),
		velocity,
		velocity,
		doneAction: 2
	);

	var synth = Klank.ar(
		[
			`[
				[freq, freq*1.5, freq*1.618],
				[env, env*0.7, env*0.5],
				[0.8, 0.4, 0.4]
			],
			`[
				[freq, freq*1.6, freq*1.8],
				[env, env*0.6, env*0.5],
				[0.8, 0.5, 0.4]
			]
		],
		Impulse.ar(1,0, env*0.9)
	);

	Out.ar(outBus, synth);
}).add;

SynthDef(\weird, {
	arg freq, velocity, inBus, outBus=0;

	var sound = Dust.ar(freq, velocity);

	Out.ar(outBus, sound!2);
}).add;

)

