# Copyright 2004-2006 Daniel F. Savarese
# Copyright 2009 Savarese Software Research Corporation
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.savarese.com/software/ApacheLicense-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

UNAME  := $(shell uname)

CC       = gcc
SHARED   = -shared
CFLAGS   = -Wall -O2 -pipe -D_REENTRANT
WINSOCK  = ws2_32
EXTRA_LDFLAGS  =
EXTRA_CPPFLAGS =
LDFLAGS  = $(EXTRA_LDFLAGS)
CPPFLAGS = $(EXTRA_CPPFLAGS)
LIBNAME  = librocksaw
LIBEXTENSION = so

ifeq ($(UNAME),Darwin)
  PLAF = unix
  JAVA_INCDIR  = $(JAVA_HOME)/include
  LIBEXTENSION = jnilib
  CFLAGS += -fPIC -DPIC
  CPPFLAGS += -I$(JAVA_INCDIR)
  LDFLAGS += -dynamiclib -noprebind -single_module -framework JavaVM
  SHARED =
else
  ifeq ($(UNAME),CYGWIN)
    override CC += -mno-cygwin
    CPPFLAGS += -D__int64="long long"
    LDFLAGS += -Wl,--kill-at -l$(WINSOCK)
    JDK_HOME := $(shell cygpath $(JDK_HOME))
    LIBNAME      = rocksaw
    LIBEXTENSION = dll
  else
    ifeq ($(UNAME),Linux)
       PLAF = linux
       CFLAGS += -fPIC -DPIC
    else
       PLAF = win32
       LDFLAGS += -l$(WINSOCK)
    endif
  endif

  JAVA_INCDIR      = $(JDK_HOME)/include
  JAVA_INCDIR_PLAF = $(JAVA_INCDIR)/$(PLAF)
  CPPFLAGS += -I"$(JAVA_INCDIR)" -I"$(JAVA_INCDIR_PLAF)"
  CFLAGS += -ansi -pthread
endif

SRC := $(wildcard *.c)
OBJ := $(SRC:%.c=%.o)

CLEAN_EXTENSIONS = o $(LIBEXTENSION)

LIBROCKSAW = $(LIBNAME).$(LIBEXTENSION)

all: $(LIBROCKSAW)

%.o: %.c
	$(CC) $(CFLAGS) $(CPPFLAGS) -c $< -o $@

$(LIBROCKSAW): $(OBJ)
	$(CC) $(SHARED) -o $@ $^ $(LDFLAGS)

clean:
	$(foreach EXT,$(CLEAN_EXTENSIONS),/bin/rm -f *.$(EXT);)
	/bin/rm -f *~
