
def check(binary):
  return binary.count("1")

  
# https://stackoverflow.com/questions/34559663/convert-decimal-to-ternarybase3-in-python
def quad(n):
    e = n//4
    q = n%4
    if n == 0:
        return '0'
    elif e == 0:
        return str(q)
    else:
        return quad(e) + str(q)
		
''''' '
a = 79

# Base 2(binary)
bin_a = bin(a)

print(bin_a)
print(int(bin_a, 2)) #Base 2(binary)
''''' 



dna = "ACTG"
subDna = "ACCTG"

d = 3
m = len(subDna)

print(quad(200))
#for x in range(m*4):


print(check("110001"))
print()

# generate d-diffenrt perms
perms = []

print("ensure proper base 4")
for x in range(4 ** d):
  perm = quad(x).zfill(d)
  print(" ", perm, end ='')
  for y in range(0,4):
    # to valid subDna
    perm = perm.replace(str(y), dna[y:y+1])
  perms.append(perm)
  
print("ensure conversion to dna")
print( perms )
	



lst = [] # of binaries
dct = {} # of things to do
newDna = []

# generate 1 masked substrings
for x in range(2 ** m):
  
  binary = (bin(x)[2:]).zfill(m)
  if(binary.count("1")==d): # 1.count = d includes up to d
    print()

    print("esnure only d many 1s, indecies, & indexes")
    print(binary + ":" , end='')
    lst = [pos for pos, char in enumerate(binary) if char == '1']
    print(lst, end='')
    #print([pos for pos, char in enumerate(binary) if char == '1'])
    dct[binary] = subDna
    
    # mask on
    for index in lst:
      print(" ", index, end='')
      # tempStr = dct[binary]
      dct[binary] = dct[binary][:index] + '1' + dct[binary][index+1:]
      #print(" ", dct[binary], end ='')
    print()

    
    print("original binary:",dct[binary], end='\t')
    # mask transformations?
    for sub in perms: # each valid subDNA
      
      stck = []
      for index in lst:
        stck.append(index)

      for char in sub: #each char in subDNA
        index = stck.pop()
        dct[binary] = dct[binary][:index] + char + dct[binary][index+1:]
      
      print(" now", dct[binary], end = '')
      newDna.append(dct[binary])

      print("\t", end='')
