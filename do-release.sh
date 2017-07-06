#!/bin/sh

exec mvn clean release:clean release:prepare release:perform
