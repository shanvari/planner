package com.company;

import java.util.ArrayList;

public class Prob {
    ArrayList<State> states = new ArrayList<>();
    ArrayList<Action> actions = new ArrayList<>();
    int scount = 2;
    State I;
    State G;
    String name;
    Prob(String n, int blk){
        name = n;
        //declare goal & initial state
        I = new State(0);
        G = new State(1);
        int j;
        String str;
        ArrayList<String> in = new ArrayList<>();
        ArrayList<String> gl = new ArrayList<>();
        switch (name){
            case "spare tire":
                in.add("atflataxle");
                in.add("atsparetrunk");
                gl.add("atspareaxle");
                break;
            case "block world":
                //table = 0;
                for(j=blk; j>0; j--) {
                    gl.add("on" + j + (j-1));
                }
                //simple initial state
                for(j=blk; j>0; j--) {
                    in.add("on" + j + 0);
                }
                break;
            case "monkey and bananas":
                //.........Pushable(Box) Climbable(Box) Graspable(Bananas)
                //1. bananas always high 2. box always low 3.heightmonkeylow != heightmonkeyhigh so we don't have heightmonkeyhigh!
                //4.bananas always graspable 5.box always pushable & climbable
                in.add("atmonkey"+0);
                in.add("atbananas"+(int)(blk/2));
                in.add("atbox"+blk);
                in.add("heightmonkeylow");
                gl.add("atmonkey"+(int)(blk/2));
                gl.add("atbox"+(int)(blk/2));
                G.n_literal.add("heightmonkeylow");
                break;
            case "dinner date":
                in.add("cleanhands");
                in.add("quiet");
                in.add("garbage");
                gl.add("dinner");
                gl.add("present");
                G.n_literal.add("garbage");
                break;
            case "link repeat":
                in.add("G*");
                in.add("G0");
                gl.add("G*");
                gl.add("G"+blk);
                break;
        }
        I.p_literal.addAll(in);
        G.p_literal.addAll(gl);
        create_action(blk);
        print();
    }
    boolean create_action(int blk){
        //temp val
        int i = 0, j = 0, k = 0, count = 0;
        String str;
        Action a;
        switch (name){
            case "spare tire":
                ArrayList<String> tire = new ArrayList<>();
                tire.add("flat");
                tire.add("spare");
                ArrayList<String> loc = new ArrayList<>();
                loc.add("axle");
                loc.add("trunk");
                for(i = 0; i < 2; i++){
                    for(j = 0; j < 2 ; j++) {
                        a = new Action("put" + tire.get(i) + loc.get(j));
                        a.A.add("at" + tire.get(i) + loc.get(j));
                        a.D.add("at"+tire.get(i)+"ground");
                        a.p_P.add("at"+tire.get(i)+"ground");
                        a.n_P.add("at" + tire.get(1-i) + loc.get(j));
                        actions.add(a);
                    }
                }
                for(i = 0; i < 2; i++){
                    for(j = 0; j < 2 ; j++) {
                        a = new Action("remove" + tire.get(i) + loc.get(j));
                        a.A.add("at" + tire.get(i) + loc.get(j));
                        a.D.add("at"+tire.get(i)+"ground");
                        a.p_P.add("at"+tire.get(i)+loc.get(j));
                        actions.add(a);
                    }
                }
                actions.add(new Action("leaveatnight"));
                break;
            case "block world":
                //0 = table
                for(i = 0; i < blk; i++){
                    for(j = 0; j < blk; j++) {
                        for(k = 0; k < blk; k++) {
                            if(i != 0 && j != k && i!= k && i != j) {
                                a = new Action("move" + i + j + k);
                                a.A.add("on" + i + k);
                                if (j != 0) a.A.add("clear" + j);
                                if (k != 0) a.D.add("clear" + k);
                                a.p_P.add("on" + i + j);
                                if (k != 0) a.p_P.add("clear" + k);
                                a.p_P.add("clear" + i);
                                if (k != 0) a.n_P.add("on*" + k);
                                a.n_P.add("on*" + i);
                                actions.add(a);
                            }
                        }
                    }
                }
                break;
            case "monkey and bananas":
                for(i = 1 ; i <= blk ; i ++)
                    for(j = 1 ; j <= blk ; j ++) {
                        if(i != j) {
                            a = new Action("go" + i + "to"+j);
                            a.A.add("atmonkey" + j);
                            a.D.add("atmonkey" + i);
                            a.p_P.add("atmonkey" + i);
                            a.p_P.add("heightmonkeylow");
                            actions.add(a);
                        }
                    }
                for(i = 1 ; i <= blk ; i ++)
                    for(j = 1 ; j <= blk ; j ++) {
                        if(i!=j) {
                            a = new Action("push" + i + "to" + j);
                            a.A.add("atmonkey" + j);
                            a.A.add("atbox" + j);
                            a.D.add("atbox" + i);
                            a.D.add("atmonkey" + i);
                            a.p_P.add("atmonkey" + i);
                            a.p_P.add("atbox" + i);
                            a.p_P.add("heightmonkeylow");
                            actions.add(a);
                        }
                    }
                for(i = 1 ; i <= blk ; i ++){
                    a = new Action("climb" + i);
                    a.D.add("heightmonkeylow");
                    a.p_P.add("atmonkey" + i);
                    a.p_P.add("atbox" + i);
                    a.p_P.add("heightmonkeylow");
                    actions.add(a);
                    }
                break;
            case "dinner date":
                a = new Action("cook");
                a.p_P.add("cleanhand");
                a.A.add("dinner");
                actions.add(a);

                a = new Action("wrap");
                a.p_P.add("quiet");
                a.A.add("present");
                actions.add(a);

                a = new Action("carry");
                a.D.add("garbage");
                a.D.add("cleanhands");
                actions.add(a);

                a = new Action("dolly");
                a.D.add("garbage");
                a.D.add("quiet");
                actions.add(a);
                break;
            case "link repeat":
                for(i = 1 ; i <= blk ; i ++){
                    a = new Action("A" + i);
                    a.p_P.add("G*");
                    a.p_P.add("G"+(i-1));
                    a.A.add("G"+i);
                    a.D.add("G*");
                    actions.add(a);
                }
                a = new Action("A*");
                a.A.add("G*");
                break;
        }
        return true;
    }
    void print(){
        System.out.println("\n" + name +" problem:");
        for(Action a: actions) {
            System.out.println("Action : " + a.name);
            System.out.println("Positive Precondition List" + a.p_P.toString());
            System.out.println("Negative Precondition List" + a.n_P.toString());
            System.out.println("Delete List" + a.D.toString());
            System.out.println("Add List" + a.A.toString());
        }
        for(State a: states)
            System.out.println("State : " + a.p_literal);
    }
    boolean check_goal_f(State s){
        //closed-world assumption rule:
        System.out.println("check_g_f ..............");
        //G.p_l in s.p_l
        for (String l: G.p_literal) {
            if(!s.p_literal.contains(l))
                return false;
        }
        //G.n_l not in s.p_l
        for (String l: G.n_literal) {
            if(s.p_literal.contains(l))
                return false;
        }
        return true;
    }
    //????
    boolean check_goal_b(State s){
        System.out.println("check_g_b ..............");
        //G.p_l in s.p_l
        for (String l: s.p_literal) {
            if(!I.p_literal.contains(l))
                return false;
        }
        for (String l: I.p_literal) {
            if(!s.p_literal.contains(l))
                return false;
        }
        //G.n_l not in s.p_l
        for (String l: s.n_literal) {
            if(!G.n_literal.contains(l))
                return false;
        }
        for (String l: G.n_literal) {
            if(!s.n_literal.contains(l))
                return false;
        }
        return true;
    }
    ArrayList<State> f_successor(State s){
        //applicable actions & apply
        ArrayList<State> child = new ArrayList<>();
        for(int j = 0 ; j < actions.size() ; j ++){
            if(is_applicable(s,actions.get(j))) {
                child.add(apply(s,actions.get(j)));
            }
        }
        if(child.isEmpty()){
            System.out.println("apply has problem.");
        }
        return child;
    }
    ArrayList<State> b_successor(State s){
        //relevant actions & regress
        ArrayList<State> child = new ArrayList<>();
        for(int j = 0 ; j < actions.size() ; j ++){
            if(is_relevant(s,actions.get(j)) > 0) {
                child.add(regress(s,actions.get(j)));
            }
        }
        if(child.isEmpty()){
            System.out.println("regress has problem.");
        }
        return child;
    }
    boolean is_applicable(State s,Action a){
        int j;
        for(j = 0 ; j < a.p_P.size();j ++){
            if(!s.p_literal.contains(a.p_P.get(j)))
                return false;
        }
        for(j = 0 ; j < a.n_P.size();j ++){
            if(!s.n_literal.contains(a.p_P.get(j)))
                return false;
        }
        return true;
    }
    int is_relevant(State s,Action a){
        int j;
        int sw = 0;
        //not exist tanaghoz
        for(j = 0 ; j < a.D.size();j ++){
            if(s.p_literal.contains(a.D.get(j)))
                return 0;
        }
        for(j = 0 ; j < a.A.size();j ++){
            if(s.n_literal.contains(a.A.get(j)))
                return 0;
        }
        //verify a p_literal
        for(j = 0 ; j < a.A.size();j ++){
            if(s.p_literal.contains(a.A.get(j)))
                sw ++;
        }
        return sw;
    }
    State apply(State p,Action a){
        State s = new State(scount);
        s.act = a;
        s.parent = p;
        s.n_literal.addAll(a.D);
        s.p_literal.addAll(s.p_literal);
        s.p_literal.addAll(a.A);
        s.p_literal.removeAll(a.D);
        scount++;
        return s;
    }
    State regress(State s, Action a){
        State r = new State(scount);
        r.act = a;
        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(s.p_literal);
        temp.removeAll(a.A);
        r.p_literal.addAll(temp);
        temp = new ArrayList<>();
        temp.addAll(s.n_literal);
        temp.removeAll(a.D);
        r.n_literal.addAll(temp);
        scount ++;
        return r;
    }
    State ignore_DL(ArrayList<State> f){
        System.out.println("ignore_dl ..............");
//delete p
        //delete a & d except g.pliteral
        State temp = null;
        int min = 10000, h;
        for(State s: f){
            h = 0;

            if(min<h){
                min = h ;
                temp = s;
            }
        }
        return temp;
    }
    State ignore_P(ArrayList<State> f){
        System.out.println("ignore_p ..............");
        State temp = null;
        int min = 10000, h;
        ArrayList<String> p_literal = G.p_literal;
        ArrayList<String> n_literal = G.n_literal;
        for(State s: f){
            h = 0;
            /*
            p_literal.removeAll(s.p_literal);
            h= p_literal.size();
            n_literal.removeAll(s.n_literal);
            h += n_literal.size();
            */
            if(min<h){
                min = h ;
                temp = s;
            }
        }
        System.out.println("h = " + min);
        return temp;
    }
}
class Action{
    String name;
    ArrayList<String> n_P = new ArrayList<>();
    ArrayList<String> p_P= new ArrayList<>();
    ArrayList<String> D= new ArrayList<>();
    ArrayList<String> A= new ArrayList<>();
    Action(String n){
        name = n;
    }
}
class State{
    int num;
    int hash_value;
    State parent;
    Action act;
    ArrayList<String> p_literal= new ArrayList<>();
    ArrayList<String>  n_literal= new ArrayList<>();
    State(int n){
        num = n;
        //????????
        hash_value = hashCode();
    }
}
