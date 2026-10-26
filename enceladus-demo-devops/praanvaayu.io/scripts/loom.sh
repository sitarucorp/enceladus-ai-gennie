#!/bin/bash

#!/bin/bash

. common.sh

echo "Detecting optimal VirtualThreadScheduler configuration..."

# Detect number of logical CPUs
CPUS=$(nproc --all)
echo "Logical CPUs: $CPUS"

# Read total available memory (in KB)
MEM_KB=$(grep MemTotal /proc/meminfo | awk '{print $2}')
MEM_GB=$((MEM_KB / 1024 / 1024))
echo "Total Memory: ${MEM_GB}GB"

# Read max user processes (soft limit)
MAX_USER_THREADS=$(ulimit -u)
echo "User Thread Limit (ulimit -u): $MAX_USER_THREADS"

# Read kernel max threads
KERNEL_THREADS_MAX=$(cat /proc/sys/kernel/threads-max)
echo "Kernel Thread Max: $KERNEL_THREADS_MAX"

# Heuristic for parallelism (number of worker threads that actively execute tasks)
# Ideal: CPUs or slightly above
RECOMMENDED_PARALLELISM=$((CPUS * 2))
echo "Suggested Parallelism: $RECOMMENDED_PARALLELISM"

# Heuristic for max pool size: up to 10k or 80% of thread limit (whichever is lower)
# Virtual threads are cheap, but too many can hurt if scheduled too fast
POOL_UPPER_LIMIT=$((MAX_USER_THREADS * 80 / 100))
RECOMMENDED_POOL_SIZE=$(($POOL_UPPER_LIMIT < 10000 ? $POOL_UPPER_LIMIT : 10000))
echo "Suggested Max Pool Size: $RECOMMENDED_POOL_SIZE"

echo "Identifying ..."
sleep 5 & spinner # ;)

# Display as Java config snippet
echo_bot "---"
echo_bot "Recommended Java Configuration:"
echo_bot "VirtualThreadScheduler scheduler = VirtualThreadScheduler.builder()"
echo_bot "    .parallelism($RECOMMENDED_PARALLELISM)"
echo_bot "    .maxPoolSize($RECOMMENDED_POOL_SIZE)"
echo_bot "    .build();"