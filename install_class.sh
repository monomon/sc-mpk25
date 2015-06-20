#!/bin/sh

#clLocation="/home/monomon/.local/share/SuperCollider/Extensions"
clLocation="/home/$USER/.local/share/SuperCollider/Extensions/classes"
mkdir -p $clLocation
echo "Copying filez..."
cp MPK.sc $clLocation

if [ $? -eq 0 ]; then
	echo "done"
else
	echo "there was a problem..."
fi
