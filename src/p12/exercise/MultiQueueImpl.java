package p12.exercise;

import java.util.*; 

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    Map<Q, Queue<T>> queues = new HashMap<>(); 

    @Override
    public Set<Q> availableQueues() {
        return queues.keySet(); 
    }

    @Override
    public void openNewQueue(Q queue) {
        if (queues.containsKey(queue)) {
            throw new java.lang.IllegalArgumentException("Queue " + queue + " is already present"); 
        }
        queues.put(queue, new LinkedList<>()); 
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        if (!queues.containsKey(queue)) {
            throw new java.lang.IllegalArgumentException("Queue " + queue + " is not present"); 
        }
        return queues.get(queue).isEmpty(); 
    }

    @Override
    public void enqueue(T elem, Q queue) {
        if (!queues.containsKey(queue)) {
            throw new java.lang.IllegalArgumentException("Queue " + queue + " is not present"); 
        }
        queues.get(queue).add(elem); 
    }

    @Override
    public T dequeue(Q queue) {
        if (!queues.containsKey(queue)) {
            throw new java.lang.IllegalArgumentException("Queue " + queue + " is not present"); 
        }
        return queues.get(queue).poll(); 
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> map = new HashMap<>(); 
        for (Q queue: this.availableQueues()) {
            map.put(queue, this.dequeue(queue)); 
        }
        return map; 
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> set = new HashSet<>(); 
        for (Q queue: this.availableQueues()) {
            set.addAll(queues.get(queue)); 
        } 
        return set; 
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if (!queues.containsKey(queue)) {
            throw new java.lang.IllegalArgumentException("Queue " + queue + " is not present"); 
        }

        List<T> allElemRemoved = new LinkedList<>(); 

        allElemRemoved.addAll(queues.get(queue)); 
        queues.get(queue).removeAll(queues.get(queue)); 

        return allElemRemoved; 
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        if (!queues.containsKey(queue)) {
            throw new java.lang.IllegalArgumentException("Queue " + queue + " is not present"); 
        }

        //this.getShortestQueue(this.availableQueues()).addAll(this.dequeueAllFromQueue(queue)); 
        var it = this.availableQueues().iterator(); 
        while (it.hasNext() && this.availableQueues().contains(queue)) {
            Q actual_queue = it.next(); 
            if (!actual_queue.equals(queue)) {
                this.queues.get(actual_queue).addAll(this.dequeueAllFromQueue(queue)); 
                this.queues.remove(queue); 
            }
        }

        if(this.availableQueues().contains(queue)) {
            throw new java.lang.IllegalStateException("there's no alternative queue for moving elements to");
        }
    }
    /* 
    private Queue<T> getShortestQueue(Set<Q> set) {
        Queue<T> shortestQueue = null;
        int minSize = Integer.MAX_VALUE;

        for (Q queue : set) {
            if (this.queues.get(queue).size() < minSize) {
                minSize = this.queues.get(queue).size();
                shortestQueue = this.queues.get(queue);
            }
        }
        
        return shortestQueue;
    }
    */
}
