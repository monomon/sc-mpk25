(
SynthDef(\bassdrum, {
	arg outBus, velocity;

	var env = EnvGen.kr(
		Env.perc(
			0.01,
			0.1*velocity+0.1,
			LinLin.kr(velocity,0,0.8,0.05,0.5)
		),
		velocity > 0,
		0.34,
		doneAction:2
	);

	var sound = SinOsc.ar(Line.kr(110,70,0.2),0,env);
	Out.ar(outBus, sound!2);
}).add;

SynthDef(\snare1, {
	arg outBus, velocity;

	var env = EnvGen.kr(
		Env.perc(
			0.01,0.2*velocity+0.1,
			LinLin.kr(velocity,0,0.8,0.05,0.8)
		),
		velocity > 0,
		doneAction:2);
	var sound = Resonz.ar(
		Mix.ar([
			SinOsc.ar(210,0,env),
			BrownNoise.ar(env)]
		),
		330,
		2,
		0.4
	);

	Out.ar(outBus, sound!2);
}).add;


SynthDef(\snare2, {
	arg outBus, velocity;

	var env = EnvGen.kr(
		Env.perc(
			0.01,0.2*velocity+0.1,
			LinLin.kr(velocity,0,0.5,0.1,1)
		),
		velocity > 0,
		0.5,
		doneAction:2
	);
	var sound = Resonz.ar(
		SinOsc.ar(410,0,0.4*env) + BrownNoise.ar(env*0.5),
		530,2
	);

	Out.ar(outBus, sound!2);
}).add;

SynthDef(\clap, {
	arg outBus, velocity;

	var env = EnvGen.kr(
		Env.perc(
			0.005,
			0.2*velocity+0.1,
			LinLin.kr(velocity, 0,0.8,0.1,0.9)
		),
		velocity > 0,
		doneAction:2
	);

	var sound = Resonz.ar(
		WhiteNoise.ar(velocity),
		1500,
		0.5,
		env
	);

	Out.ar(outBus, sound!2);
}).add;

SynthDef(\hat1, {
	arg outBus, velocity;

	var env = EnvGen.kr(
		Env.perc(
			0.02,
			0.5*velocity + 0.1,
			LinLin.kr(velocity,0,0.8,0.1,0.7)
		),
		velocity > 0,
		0.6,
		doneAction:2
	);

	var noiseRatio = 0.7;
	var sound = Resonz.ar(
		WhiteNoise.ar(env*noiseRatio) + Saw.ar(9000,env*(1-noiseRatio)),
		BrownNoise.kr(10,15000),
		1.3,
		env
	);

	Out.ar(outBus, sound!2);
}).add;

SynthDef(\hat2, {
	arg outBus, velocity;

	var env = EnvGen.kr(
		Env.perc(
			0.02,
			0.8*velocity + 0.1,
			LinLin.kr(velocity,0,0.8,0.1,0.8)
		),
		velocity > 0,
		0.6,
		doneAction:2
	);

	var noiseRatio = 0.9;
	var sound = Resonz.ar(
		WhiteNoise.ar(env*noiseRatio) + Saw.ar(12000,env*(1-noiseRatio)),
		BrownNoise.kr(10,17000),
		1.2,
		env
	);

	Out.ar(outBus, sound!2);
}).add;

SynthDef(\drumFx, {
	arg inBus, outBus;

	var sound = FreeVerb2.ar(
		In.ar(inBus,2),
		In.ar(inBus,2),
		0.33,0.3,0.7
	);

	Out.ar(outBus,sound);
}).add;
)