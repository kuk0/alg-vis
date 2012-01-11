from numpy import *
from pyks import *
from cmath import *
from random import *
from copy import copy

radius= 10
nodes = {}
edges = []
nodes2 = {}

def clear():
  global nodes, nodes2, edges
  nodes2 = nodes
  nodes = {}
  edges = []

def Node(i, key, z):
  #circle(z, 0.33)
  nodes[i] = (key,z)

def Edge(i, j, s=0):
  edges.append((i, j, s))

def Subtree(z, t, a=0.5, d=1.7):
  poly([z, z-a-d*1j, z+a-d*1j])
  label(z-d*0.7j, t)

def n(i, id=False, empty=False, fill=100):
  key, z = nodes[i]
  if empty: key = ""
  else: key = str(key)
  node(z, radius, key, fill)
  if id: label(z+radius*(1-1j), "\scriptsize "+str(i))

def nz(i): return nodes[i][1]
def nx(i): return nz(i).real
def ny(i): return nz(i).imag
def nzk(k):
  for key,z in nodes.itervalues():
    if key==k: return z

def m(i):
  for k in xrange(0,10):
    setcolor(color.grey((9-k)*0.1))
    node(mid(nodes[i][1]-75,nodes2[i][1],9-k,k),radius, "")

def e(i,j,st):
  if st == 3:
    z,w = nodes[i][1], nodes[j][1]
    m,u = mid(z,w), (w-z)/abs(w-z) * 3j
    line(z,w)
    line(m+u,m-u)
  else:
    line(nodes[i][1], nodes[j][1], s=st)

def drawnodes(id=False, empty=False):
  for i in nodes.iterkeys(): n(i,id,empty)

def drawnodes2(id=False, empty=False):
  for i in nodes.iterkeys(): m(i)

def drawedges():
  for i,j,s in edges: e(i,j,s)


def el3(i,j,f=85):
  el2(nodes[i][1], nodes[j][1],f)
def el2(z,w,f=95):
  u = (w-z)/abs(w-z)
  ellipse(mid(z,w), (w-z)/2+1.2*radius*u, 2*radius, fill=f)
def el(i,j,f=95):
  el2(nzk(i), nzk(j), f)

def rot(i,j,poc=None,f=95):
  rot2(nzk(i), nzk(j), poc,f)
def rot2(z,w,poc=None,f=95):
  u = (w-z)/abs(w-z)
  line(w,z, lw=30*0.032, col=color.grey(f*0.01))
  if poc == None: return
  if poc == 1: s = 'I.'
  else: s = 'II.'
  label(mid(w,z)-u*8j, s)
#
def ell3(i,j,k,f=85):
  ell2(nodes[i][1],nodes[j][1],nodes[k][1], f)
def ell2(v,w,z,f=85):
  line(v,w, lw=35*0.032, col=color.grey(f*0.01))
  line(w,z, lw=35*0.032, col=color.grey(f*0.01))
def ell(i,j,k,f=85):
  ell2(nzk(i), nzk(j), nzk(k), f)


def CH(p):
      points = copy(p)
      r0 = min([(z.real,z.imag) for z in points])
      r0 = r0[0]+r0[1]*1j
      hull = [r0]
      r,u = r0,None
      remainingPoints = [x for x in points if x not in hull]
      while u != r0 and remainingPoints:
            u = choice(remainingPoints)
            for t in points:
                  if t != u and t != r and ((u-r)*(t-u).conjugate()).imag > 0:
                        u = t
            r = u
            if r in points: points.remove(r)
            hull.append(r)
            if r in remainingPoints: remainingPoints.remove(r)
      return hull

def joinch(a,b,m):
  return [z for z in a + b if (z in a) ^ (z in b) ^ (z in m)]
