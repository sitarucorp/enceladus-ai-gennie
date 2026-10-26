#! /bin/bash

# sleep 5 & spinner # ;)
spinner()
{
    local pid=$!
    local delay=0.75
    local spinstr='|/-\'
    while [ "$(ps a | awk '{print $1}' | grep $pid)" ]; do
        local temp=${spinstr#?}
        printf " [%c]  " "$spinstr"
        local spinstr=$temp${spinstr%"$temp"}
        sleep $delay
        printf "\b\b\b\b\b\b"
    done
    printf "    \b\b\b\b"
}

echo_bot() {
  local line="$1"
  for word in $line; do
    echo -n "$word "
    sleep 0.2
  done
  echo
}
