# -*- coding: utf-8 -*-
from pyx import *
from cmath import *

text.preamble(r"""
\parindent=0pt\
%\let\phi\varphi 
\font\fivebf =cmbx10  scaled 500 
\font\sevenbf=cmbx10  scaled 700 
\font\tenbf  =cmbx10             
\font\fivemb =cmmib10 scaled 500 
\font\sevenmb=cmmib10 scaled 700 
\font\tenmb  =cmmib10            
\def\boldmath{\textfont0=\tenbf           \scriptfont0=\sevenbf 
              \scriptscriptfont0=\fivebf  \textfont1=\tenmb
              \scriptfont1=\sevenmb       \scriptscriptfont1=\fivemb}
""")
C=canvas.canvas()
u=1; v=1; addr=""

cc = C
current_color = color.rgb.black
current_pos = 0j

def setuv(uu,vv): global u,v; u=uu; v=vv;
def setwdir(wdir):
  global addr
  addr = wdir
  if addr[-1] != '/': addr = addr+'/'

def setcolor (c = None):
  global current_color;
  if c == None: current_color = color.rgb.black
  else: current_color= c;

def setcanvas (canvas = None):
  global cc;
  if canvas == None: cc = C
  else: cc = canvas

def newcanvas():
  global cc;
  cc = canvas.canvas()
  return cc

def cpaste (z=0j, a=0, f=1.0, c = None):
  global cc, u, v, C;
  z = complex(z)
  if c == None: C.insert (cc, [trafo.scale (f), trafo.rotate (a), trafo.translate (z.real*u, z.imag*v)])
  else: c.insert (cc, [trafo.scale (f), trafo.rotate (a), trafo.translate (z.real*u, z.imag*v)])

def paste (z=0j, a=0, f=1.0, c = None):
  global cc, C; 
  cpaste (z, a, f, c);
  if c == None: cc = C
  else: cc = c

def new(): global C, cc; C = cc = canvas.canvas();
def newa(x,y,xt="$x$",yt="$y$"): new(); axes(-x,-y,x,y,xt,yt);
def newp(x,y,xt="",yt=""): new(); plainaxes(-x,-y,x,y,xt,yt);
def newpa(x,y,xt="$x$",yt="$y$"): new(); axes(0,0,x,y,xt,yt);
def newpp(x,y,xt="",yt=""): new(); plainaxes(0,0,x,y,xt,yt);
def newc(x,y): new(); axes(-x,-y,x,y,r"$\Re$",r"$\Im$");
def save(f):
  global C, cc;
  #paste();
  C.writePDFfile(addr+f);

def circ(z, col=None, fill=100, r=0.06):
  global cc, u, v, current_color;
  if col == None: col = current_color
  try:
    for w in z: circ(w, col, fill, r)
  except TypeError:
    z = complex(z)
    #cc.stroke (path.circle(u*z.real, v*z.imag, 0.06*u), [col]);
    cc.draw(path.circle(u*z.real,v*z.imag,r*u), [deco.stroked(), deco.filled([color.grey(0.01*fill)]), col])
def dot(z, col=None, r=0.06):
  global cc, u, v, current_color;
  if col == None: col = current_color
  try:
    for w in z: dot(w, col, r)
  except TypeError:
    z = complex(z)
    print '.',
    cc.fill (path.circle(u*z.real, v*z.imag, r*u), [col]);
def bdot(z, col=None): dot(z,col,0.09)
def node(z,r,t="",col=100):
  global cc, u, v
  cc.draw(path.circle(u*z.real,v*z.imag,u*r), [deco.stroked(), deco.filled([color.grey(0.01*col)]), current_color])
  label(z,t)

# styl - 0, 1, 2 = solid, dashed, dotted
st = [style.linestyle.solid, style.linestyle.dashed, style.linestyle.dotted]
# line(z,w,s) kde z, w su komplexne cisla, nakresli ciaru od z ku w stylom s
# r- relative, b- bold
# arrow(z,w,s) to iste, ale sipka; arrow2 je obojstranna sipka
def line (z, w=0j, s=0, col=None, lw=0.02):
  global cc, u, v, current_color
  if col == None: col = current_color
  try:
    for pz in z: line(pz, w, s, col, lw)
  except TypeError:
    try:
      for pw in w: line(z, pw, s, col, lw)
    except TypeError:
      z = complex(z); w = complex(w)
      cc.stroke (path.line(u*z.real, v*z.imag, u*w.real, v*w.imag), [st[s], col, style.linewidth(lw), style.linecap.round])
def bline (z, w=0j, s=0, col=None): line (z, w, s, col, 0.05)
def rline (z, w, s=0, col=None, lw=0.02): line (z, z+w, s, col, lw)
def rbline (z, w, s=0, col=None, lw=0.02): bline (z, z+w, s, col, lw)
def moveto (z):
  global current_pos
  current_pos = complex(z)
def lineto (z, s=0, col=None, lw=0.02):
  global current_pos
  z = complex(z)
  line (current_pos, z, s, col, lw)
  current_pos = z
def blineto (z, s=0, col=None): lineto (z, s, col, 0.05)
def arrow(z, w, s=0, col=None, lw = 0.02):
  global cc, u, v, current_color
  if col == None: col = current_color
  z = complex(z); w = complex(w)
  cc.stroke (path.line (u*z.real, v*z.imag, u*w.real, v*w.imag), [st[s], deco.earrow.normal, col, style.linewidth(lw)]);
def barrow(z, w, s=0, col=None): arrow (z, w, s, col, 0.05)
def arrow2(z, w=0j, s=0, col=None, lw = 0.02):
  global cc, u, v, current_color
  if col == None: col = current_color
  z = complex(z); w = complex(w)
  cc.stroke (path.line (u*z.real, v*z.imag, u*w.real, v*w.imag), [st[s], deco.earrow.normal, deco.barrow.normal, col, style.linewidth(lw)]);
def barrow2(z, w=0j, s=0, col=None): arrow2 (z, w, s, col, 0.05)

# label(z,t) - text t na poziciu z; t/b/l/r top/bottom/left/right + kombinacie
def label(z,t): global cc,u,v; cc.text(u*z.real,v*z.imag,t,[text.halign.boxcenter,text.valign.middle]);
def labelt(z,t): global cc,u,v; cc.text(u*z.real,v*z.imag+.25,t,[text.halign.boxcenter,text.valign.baseline]);
def labelb(z,t): global cc,u,v; cc.text(u*z.real,v*z.imag-.4,t,[text.halign.boxcenter,text.valign.baseline]);
def labell(z,t): global cc,u,v; cc.text(u*z.real-.25,v*z.imag,t,[text.halign.boxright,text.valign.middle]);
def labelr(z,t): global cc,u,v; cc.text(u*z.real+.25,v*z.imag,t,[text.halign.boxleft,text.valign.middle]);
def labeltr(z,t): global cc,u,v; cc.text(u*z.real+.2,v*z.imag+.2,t,[text.halign.boxleft,text.valign.bottom]);
def labeltl(z,t): global cc,u,v; cc.text(u*z.real-.2,v*z.imag+.2,t,[text.halign.boxright,text.valign.bottom]);
def labelbr(z,t): global cc,u,v; cc.text(u*z.real+.2,v*z.imag-.2,t,[text.halign.boxleft,text.valign.top]);
def labelbl(z,t): global cc,u,v; cc.text(u*z.real-.2,v*z.imag-.2,t,[text.halign.boxright,text.valign.top]);
def labela(w, a, t): # TODO: rozsirit uhly a upravit tie vzdialenosti
  while a < 0: a += 360
  while a > 360: a -= 360
  if a > 315 or a < 45:
    labelr(w,t)
  elif 45 <= a < 135:
    labelt(w,t)
  elif 135 <= a < 215:
    labell(w,t)
  else:
    labelb(w,t)


def rect(z,w,s=0): rrect(z,w-z,s);
def rrect(z,w,s=0): global cc,u,v; cc.draw(path.rect(u*z.real,v*z.imag,u*w.real,v*w.imag), [deco.stroked(),st[s]]);
def block(z,w,col=None,t=""): rblock(z,w-z,t,col);
def rblock(z,w,t="",col=100):
  global cc, u, v, current_color
  cc.draw(path.rect(u*z.real,v*z.imag,u*w.real,v*w.imag), [deco.stroked(), deco.filled([color.grey(0.01*col)]), current_color])
  label(z+w/2,t)

def circle(z, r, col=None, fill=100, s=0): ellipse(z, r, r, col, fill, s)
def ellipse (z, ra, rb, col=None, fill=100, s=0):
  global cc, u, v, current_color;
  if col == None: col = current_color
  z, ra = complex(z), complex(ra)
  rb = (ra/abs(ra)*1j) * rb
  cc.draw(path.circle(0,0,1), [deco.stroked(), deco.filled([color.grey(0.01*fill)]), color.transparency(0.5), col, st[s], trafo.scale(sx=abs(ra)*u, sy=abs(rb)*v), trafo.rotate(phase(ra.real*u+ra.imag*v*1j)*180/pi), trafo.translate(u*z.real, v*z.imag)])

def poly(p, s=0, col=None, fill=None, closed=True, lw = 0.02):
  global cc, u, v, current_color
  if col == None: col = current_color
  l = path.path(path.moveto(u*p[0].real,v*p[0].imag))
  for pt in p[1:]:
    l.append(path.lineto(u*pt.real,v*pt.imag))
  if closed: l.append(path.closepath())
  if fill == None: cc.stroke(l, [st[s], col, style.linewidth(lw)])
  else: cc.fill(l, [st[s], col, style.linewidth(lw), deco.stroked(), deco.filled([fill])])
def fpoly(p, s=0, col=None, lw = 0.02): poly(p, s, col, color.grey(0.01*col), True, lw);
def rpoly(p):
  global cc,u,v
  l = path.path(path.moveto(u*p[0].real,v*p[0].imag))
  for pt in p[1:]:
    l.append(path.rlineto(u*pt.real,v*pt.imag))
  l.append(path.closepath())
  cc.stroke(l);
def rfpoly(p,col=0):
  global cc,u,v
  l = path.path(path.moveto(u*p[0].real,v*p[0].imag))
  for pt in p[1:]:
    l.append(path.rlineto(u*pt.real,v*pt.imag))
  l.append(path.closepath())
  cc.fill(l,[deco.stroked(),deco.filled([color.grey(0.01*col)])]);

def angle(z, r, a1, a2, s=0, col=None, lw=0.02):
  global cc, u, current_color;
  if col==None: col = current_color
  cc.stroke(path.path(path.arc(u*z.real,v*z.imag,r*u,a1,a2)),[st[s], col, style.linewidth(lw)]);

def aarrow(z, r, a1, a2, s=0, col=None, lw=0.02): #TODO: toto nejak lepsie
  global cc, u, current_color;
  if col==None: col = current_color
  cc.stroke(path.path(path.arc(u*z.real,v*z.imag,r*u,a1,a2)),[st[s], deco.earrow.normal, col, style.linewidth(lw)]);

def raarrow(z, r, a1, a2, s=0, col=None, lw=0.02): #TODO: toto nejak lepsie
  global cc, u, current_color;
  if col==None: col = current_color
  cc.stroke(path.path(path.arc(u*z.real,v*z.imag,r*u,a1,a2)),[st[s], deco.barrow.normal, col, style.linewidth(lw)]);

def plainaxes(x1,y1,x2,y2,xt="", yt=""):
  global cc, u, v
  arrow2(x1-.5+0j, x2+.5+0j); arrow2((y1-.5)*1j,(y2+.5)*1j)
  labelr(x2+0.5+0j,xt); labelt((y2+0.5)*1j,yt)

def axes (x1,y1,x2,y2,xt="$x$",yt="$y$"):
  global cc,u,v
  plainaxes(x1,y1,x2,y2,xt,yt)
  for i in xrange(x1,x2+1):
    cc.stroke(path.line(u*i,0.1,u*i,-0.1))
    if i==0: cc.text(u*i-.1,-.4,r"0",[text.halign.boxright]);
    else: cc.text(u*i,-.4,str(i),[text.halign.boxcenter]);
  for i in xrange(y1,y2+1):
    cc.stroke(path.line(-0.1,u*i,0.1,u*i))
    if i!=0:
      if i==1: cc.text(-.25,u,"$i$",[text.halign.boxright,text.valign.middle]);
      elif i==-1: cc.text(-.25,-u,"$-i$",[text.halign.boxright,text.valign.middle]);
      else: cc.text(-.25, u*i,"$"+str(i)+"i$",[text.halign.boxright,text.valign.middle]);

def parrow (p, s=0, col=None, lw = 0.02):
  global cc, current_color;
  if col == None: col = current_color
  cc.stroke(p, [st[s], deco.earrow.normal, col, style.linewidth(lw)]);
def curve (w, x, y, z, s=0, col=None, lw = 0.02):
  global cc, u, v, current_color
  if col==None: col = current_color
  cc.stroke (path.curve (u*w.real, v*w.imag, u*x.real, v*x.imag, u*y.real, v*y.imag, u*z.real, v*z.imag,),
             [st[s], col, style.linewidth(lw)])
def bcurve (w, x, y, z, s = 0, col = None): curve (w, x, y, z, s, col, 0.05)
def carrow (w, x, y, z, s = 0, col = None, lw = 0.02):
  global cc, u, v, current_color
  if col==None: col = current_color
  cc.stroke (path.curve (u*w.real, v*w.imag, u*x.real, v*x.imag, u*y.real, v*y.imag, u*z.real, v*z.imag),
             [st[s], deco.earrow.normal, col, style.linewidth(lw)])
def carrow2 (w, x, y, z, s = 0, col = None, lw = 0.02):
  global cc, u, v, current_color
  if col==None: col = current_color
  cc.stroke (path.curve (u*w.real, v*w.imag, u*x.real, v*x.imag, u*y.real, v*y.imag, u*z.real, v*z.imag),
             [st[s], deco.earrow.normal, deco.barrow.normal, col, style.linewidth(lw)])

def vlnka (w, z, n = 5, s = 0, col = None, lw = 0.02): #TODO: vseobecnejsie
  global cc, u, v, current_color
  if col==None: col = current_color
  l = path.path(path.moveto(u*w.real,v*w.imag))
  norm = (z-w) / abs(z-w) * 1j
  for k in xrange(0, 100):
    pt = mid(w,z,k,100-k) + 0.06 * norm * sin(2*pi*n*(k/100.0))
    l.append(path.lineto(u*pt.real,v*pt.imag))
  l.append(path.lineto(u*z.real,v*z.imag))
  cc.stroke(l, [col, deformer.smoothed(2.5), style.linewidth(lw)])

def vlnka2 (C, n = 5, s = 0, col = None, lw = 0.02): #TODO: vseobecnejsie
  global cc, u, v, current_color
  if col==None: col = current_color
  x,y = C.atbegin()
  w = 100*x.t + 100j*y.t
  x,y = C.atend()
  z = 100*x.t + 100j*y.t
  l = path.path(path.moveto(u*w.real,v*w.imag))
  for k in xrange(0, 100):
    x,y = C.at(k/100.0*C.arclen())
    x2,y2 = C.at((k+1)/100.0*C.arclen())
    z1 = 100*x.t + 100j*y.t
    z2 = 100*x2.t + 100j*y2.t
    norm = (z2-z1) / abs(z2-z1) * 1j
    pt = z1 + 0.06 * norm * sin(2*pi*n*(k/100.0))
    l.append(path.lineto(u*pt.real,v*pt.imag))
  l.append(path.lineto(u*z.real,v*z.imag))
  cc.stroke(l, [col, deformer.smoothed(2.5), style.linewidth(lw)])

def cis(a): return exp((1j/180.0*pi)*a)

# TODO:
def frange(a, b, incr):
  while a < b:
    yield a
    a += incr

# point m such that am : mb = da : db
# e.g. mid(a,b) is in the middle of a, b
#      mid(a,b,2,1) is m 2/3 way from a to b (am : mb = 2 : 1)
def mid(a, b, da=1, db=1):
  return (a*db + b*da)/float(da+db)

def czip (x, y):
  return [u+1j*v for u,v in zip(x,y)]

def ptsdir (z, d, n): # n points starting from z in direction d
  p = [z]
  for i in xrange(0,n-1):
    p.append(p[-1]+d)
  return p

def ptsline (w, z, n, bincl=True, eincl=True): # n points from w to z
  p = []
  if not bincl:
    w = w + (z-w)/float(n)
  if not eincl:
    z = z + (w-z)/float(n)
  for i in xrange(0,n):
    p.append(w + (z-w)*(i/float(n-1)))
  return p

def ptscirc (z, r, n):
  p = [z+r*exp(2j*pi*k/n) for k in xrange(0,n)]
  # todo
  # asi aj zac/kon. uhol
  return p 


def carrow3 (w, z, b=0.3, s = 0, col = None, lw = 0.02):
  m, u = mid(w,z), (z-w)*0.5j*b
  carrow(w, m+u, m+u, z, s, col, lw)
