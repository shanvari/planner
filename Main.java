package com.company;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LocalTime start =  java.time.LocalTime.now();
        Scanner scanner = new Scanner(System.in);
        //model domains:
        Prob p1 = new Prob("spare tire",0);
        Prob p2 = new Prob("block world",5);
        Prob p3 = new Prob("monkey and bananas",10);
        Prob p4 = new Prob("dinner date",0);
        Prob p5 = new Prob("link repeat",10);

        //find solution:

        //State ans1 = backward(p1);
        State ans1 = forward(p1,true);
        //true = ignore precondition heuristic & false = ignore delete list heuristic
        if(ans1 != null)    print_solution(ans1,true);
        else     System.out.println("Sorry we can't find solution......");
        LocalTime end =  java.time.LocalTime.now();
        System.out.println("Planner time = " + (Duration.between(end, start).toSeconds()));
    }
    static void print_solution(State ans,boolean fb) {
        System.out.println("\nSolution :");
        int i = 0;
        String[] sol = new String[100];
        while (ans != null) {
            sol[i] = ans.act.name;
            i ++;
            ans = ans.parent;
        }
        i --;
        for (; i >= 0 ; i --){
            System.out.println(i + " : " + sol[i]);
        }
    }
    public static State backward(Prob p){
        //relevant actions & regress
        System.out.println("backward ..............");
        ArrayList<State> fringe = new ArrayList<>();
        ArrayList<Integer> explored = new ArrayList<>();
        State s;
        fringe.add(p.G);
        while (!fringe.isEmpty()) {
            s = fringe.get(fringe.size()-1);
            if(p.check_goal_b(s))   return s;
            if(explored.contains(s.hash_value)) continue;
            ArrayList<State> ch = p.b_successor(s);
            for(State c: ch)
                fringe.add(c);
            explored.add(s.hash_value);
        }
        return null;
    }
    public static State forward(Prob p, boolean p_dl){
        System.out.println("forward ..............");
        ArrayList<State> fringe = new ArrayList<>();
        ArrayList<Integer> explored = new ArrayList<>();
        State s;
        fringe.add(p.I);
        while (!fringe.isEmpty()) {
            if (p_dl) {
                s = p.ignore_P(fringe);
            } else {
                s = p.ignore_DL(fringe);
            }
            fringe.remove(s);
            if(p.check_goal_f(s))   return s;
            if(explored.contains(s.hash_value)) continue;
            ArrayList<State> ch = p.f_successor(s);
            for(State c: ch)
                fringe.add(c);
            explored.add(s.hash_value);
        }
        return null;
    }
}
