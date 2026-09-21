# Static descriptor inventory; not a JVM linker or runtime certification.
import struct,zipfile,json,sys
class Class:
 def __init__(self,b):
  self.b=b;self.p=8;self.cp=[None]*self.u2();i=1
  while i<len(self.cp):
   t=self.u1()
   if t==1:
    n=self.u2();self.cp[i]=self.take(n).decode('utf-8','replace')
   elif t in (7,8,16,19,20):self.cp[i]=(t,self.u2())
   elif t in (9,10,11,12,17,18):self.cp[i]=(t,self.u2(),self.u2())
   elif t in (3,4):self.take(4)
   elif t in (5,6):self.take(8);i+=1
   elif t==15:self.cp[i]=(t,self.u1(),self.u2())
   else:raise ValueError(t)
   i+=1
  self.access=self.u2();self.name=self.nameof(self.u2());s=self.u2();self.parent=self.nameof(s) if s else None
  self.interfaces=[self.nameof(self.u2()) for _ in range(self.u2())]
  self.fields=self.members();self.methods=self.members()
 def take(self,n):v=self.b[self.p:self.p+n];self.p+=n;return v
 def u1(self):return self.take(1)[0]
 def u2(self):return struct.unpack('>H',self.take(2))[0]
 def u4(self):return struct.unpack('>I',self.take(4))[0]
 def nameof(self,n):return self.cp[self.cp[n][1]]
 def members(self):
  result=[]
  for _ in range(self.u2()):
   access=self.u2();name=self.cp[self.u2()];desc=self.cp[self.u2()];attrs={}
   for _ in range(self.u2()):
    key=self.cp[self.u2()];attrs[key]=self.take(self.u4())
   result.append((name,desc,access,attrs))
  return result
 def refs(self):
  for c in self.cp:
   if isinstance(c,tuple) and c[0] in (9,10,11):
    nt=self.cp[c[2]];yield c[0],self.nameof(c[1]),self.cp[nt[1]],self.cp[nt[2]]
if __name__=='__main__':
 source,target=sys.argv[1:];cache={};out=[]
 with zipfile.ZipFile(target) as z,zipfile.ZipFile(source) as src:
  def read(n):
   if n not in cache:
    try:cache[n]=Class(z.read(n+'.class'))
    except KeyError:cache[n]=None
   return cache[n]
  def resolve(owner,name,desc,kind,seen=None):
   seen=set() if seen is None else seen
   if owner in seen:return False
   seen.add(owner);c=read(owner)
   if not c:return False
   if any(n==name and d==desc for n,d,a,attrs in (c.fields if kind==9 else c.methods)):return True
   return any(resolve(p,name,desc,kind,seen) for p in [c.parent]+c.interfaces if p)
  refs=set()
  for n in src.namelist():
   if n.endswith('.class'):
    for kind,owner,name,desc in Class(src.read(n)).refs():
     if owner.startswith('com/cobblemon/'):refs.add((kind,owner,name,desc))
  for kind,owner,name,desc in sorted(refs):
   ok=resolve(owner,name,desc,kind);out.append(dict(owner=owner,name=name,descriptor=desc,found=ok))
   print(('OK ' if ok else 'MISSING ')+owner+'.'+name+desc)
  missing=sum(not x['found'] for x in out)
  print('TOTAL',len(out),'MISSING',missing)
  sys.exit(1 if missing else 0)
